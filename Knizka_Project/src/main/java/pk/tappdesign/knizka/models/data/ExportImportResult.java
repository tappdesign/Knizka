package pk.tappdesign.knizka.models.data;


public class ExportImportResult {

   private int successfully;
   private int failed;

   public ExportImportResult()
   {
      failed = 0;
      successfully = 0;
   }

   public int getSuccessfully() {
      return successfully;
   }

   public int getFailed() {
      return failed;
   }

   public void addSuccessfully()
   {
      successfully++;
   }

   public void addFailed()
   {
      failed++;
   }

   public int getAll()
   {
      return successfully + failed;
   }

   public boolean isSuccess()
   {
      return failed == 0 ? true : false;
   }
}
