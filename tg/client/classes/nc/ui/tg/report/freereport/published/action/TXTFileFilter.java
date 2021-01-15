package nc.ui.tg.report.freereport.published.action;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class TXTFileFilter extends FileFilter
{
  public boolean accept(File f)
  {
    if (f.isDirectory()) {
      return true;
    }
    String extension = null;
    String name = f.getName();
    int pos = name.lastIndexOf('.');
    if ((pos > 0) && (pos < name.length() - 1)) {
      extension = name.substring(pos + 1);
    }
    if ((extension != null) && (extension.equalsIgnoreCase("txt"))) {
      return true;
    }
    return false;
  }

  public String getDescription()
  {
    return "TXT Files";
  }
}