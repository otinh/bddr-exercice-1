package crawler;

import java.util.HashSet;
import java.util.Set;

public class Crawler {

   private final String URL = "http://www.dxcontent.com/SDB_SpellBlock.asp?SDBID=";
   private final int MAX_ID = 1975;

   private Set<String> visitedPages = new HashSet<>();

   public String getURL(int id) {
       return URL + id;
   }

   public void searchSpells() {

      for (int i = 1; i <= MAX_ID; i++) {
         System.out.println(getURL(i));
      }

   }

}
