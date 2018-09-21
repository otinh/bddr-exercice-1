package crawler;

public class Crawler {

   class Website {
      private final int MAX_SPELL_ID = 1975;
      private String URL = "http://www.dxcontent.com/SDB_SpellBlock.asp?SDBID=";

      public String url(int id) {
         try {
            return this.URL + id;
         } catch (Exception e) {
            System.out.println("Max ID reached at id=" + id);
            return "";
         }
      }
   }



}
