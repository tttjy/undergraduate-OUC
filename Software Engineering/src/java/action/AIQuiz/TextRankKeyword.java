package com.qst.action.AIQuiz;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import java.util.*;

/**
 * 关键词提取，但是servlet执行不了这个工具类，不知道为什么
 */
public class TextRankKeyword {
  public static String extractKeywords(String title, String content) {
    int nKeyword = 10;
    float d = 0.85f;
    int max_iter = 200;
    float min_diff = 0.001f;
    List<Term> termList = HanLP.segment(title + content);
    List<String> wordList = new ArrayList<>();

    for (Term term : termList) {
      if (shouldInclude(term)) {
        wordList.add(term.word);
      }
    }

    Map<String, Set<String>> words = new HashMap<>();
    Queue<String> queue = new LinkedList<>();

    for (String word : wordList) {
      if (!words.containsKey(word)) {
        words.put(word, new HashSet<>());
      }

      queue.offer(word);

      if (queue.size() > 5) {
        queue.poll();
      }

      for (String w1 : queue) {
        for (String w2 : queue) {
          if (!w1.equals(w2)) {
            words.get(w1).add(w2);
            words.get(w2).add(w1);
          }
        }
      }
    }

    Map<String, Float> score = new HashMap<>();

    for (int i = 0; i < max_iter; ++i) {
      Map<String, Float> m = new HashMap<>();
      float max_diff = 0;

      for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
        String key = entry.getKey();
        Set<String> value = entry.getValue();
        m.put(key, 1 - d);

        for (String other : value) {
          int size = words.get(other).size();

          if (key.equals(other) || size == 0)
            continue;

          m.put(key, m.get(key) + d / size * (score.get(other) == null ? 0 : score.get(other)));
        }

        max_diff = Math.max(
            max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
      }

      score = m;

      if (max_diff <= min_diff) {
        break;
      }
    }

    List<Map.Entry<String, Float>> entryList = new ArrayList<>(score.entrySet());

    entryList.sort((o1, o2) -> o1.getValue().compareTo(o2.getValue()) > 0 ? -1 : 1);

    StringBuilder result = new StringBuilder();

    for (int i = 0; i < nKeyword; ++i) {
      result.append(entryList.get(i).getKey()).append(' ');
    }

    return result.toString().trim();
  }

  public static void main(String[] args) {
    String content = "";
    String keywords = extractKeywords("",
        "根据您提供的问题和答案，可以看出您对关系最密切的人感到不满意，但看到最近拍摄的照片感觉很好。另外，您对不同的事物也有不同的看法和喜好，例如对炸酱面和拉面的喜好因人而异，对旅游、游戏、原神、学科、小动物、可乐以及噩梦的看法也各不相同。因此，很难用一句话总结您的情绪。");
    System.out.println(keywords);
  }

  public static boolean shouldInclude(Term term) {
    return CoreStopWordDictionary.shouldInclude(term);
  }
}
