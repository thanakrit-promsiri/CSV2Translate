package csv2translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Translator {

    public static final String FILEREPO = "T24n1465_001";
    public static final String PREPARE = "/prepare";
    public static final String OUTPUT = "/output";
    public static final String INPUT = "/input";
    public static final String CONFIG = "/config";

    public static final String FILEREMOVE = CONFIG + "/txt2remove.txt";
    public static final String CSVTRANSLATE = CONFIG + "/csv2translate.csv";

    public static final String FREMOVE = "2";
    public static final String FINSERTSPACE = "3";

    public static void main(String[] args) {
        Translator translator = new Translator();
        translator.processPrepareFile();
        translator.processPrepareData();
    }

    public void processPrepareFile() {
        String[] directory = this.checkDirectory(FILEREPO);
        for (String file : directory) {
            String path = FILEREPO + INPUT + "/" + file;
            String txt = this.getFile(path);
            this.processPrepare(txt.toLowerCase(), file);

        }
    }

    public void processPrepareData() {
        File file2 = new File(FILEREPO + PREPARE);
        System.out.println(FILEREPO + PREPARE);
        System.out.println("___________");
        this.printFile(file2.list());
        for (String file : file2.list()) {
            String prepare = FILEREPO + PREPARE + "/" + file;
            System.out.println(prepare);
            String txt = this.getFile(prepare);
        }
        System.out.println("___________");
    }

    protected String processPrepare(String txt, String file) {

        txt = this.textRemove(txt);
        txt = this.textInsertSpace(txt);
        this.writeUsingFiles(txt, FILEREPO + PREPARE + "/_" + file);
        return txt;
    }

    protected String textRemove(String txt) {
        String path = FILEREPO + FILEREMOVE;

        return textReplaceByConfigFile(txt, path, FREMOVE);
    }

    protected String textInsertSpace(String txt) {
        String path = FILEREPO + CSVTRANSLATE;
        txt = textReplaceByConfigFile(txt, path, FINSERTSPACE);
        return txt;
    }

    protected String textReplaceByConfigFile(String txt, String path, String flagReplace) {

        String replacement = "";

        String texOrigin = getFile(path);
        List<String> txtConfList = textSplitLine(texOrigin);
        for (String txtConf : txtConfList) {
            if (!txtConf.equalsIgnoreCase("")) {
                if (FINSERTSPACE.equalsIgnoreCase(flagReplace)) {
                    // txt = txt.replaceAll(txtConf, " " + txtConf + " ");
                    txt = this.textReplaceSpace(txt, txtConf);
//                    System.err.println(txtConf);
                } else {
                    txt = txt.replaceAll(txtConf, replacement);
                }
            }
        }
        return txt;
    }

    protected String textReplaceSpace(String txt, String txtConf) {
        String[] strArr = txtConf.split(",");
        if (strArr.length > 1) {
            if(!strArr[1].trim().isEmpty())
            return txt.replaceAll(strArr[0], "[" + strArr[1] + "]");

        }
        return txt.replaceAll(txtConf, " " + txtConf + " ");

    }

    protected List<String> textSplitLine(String str) {
        String[] strLineArr = str.split("\n");
        return Arrays.asList(strLineArr);
    }

    protected String[] checkDirectory(String str) {
        this.setDirectory(str);
        this.setFilePrepare(str, PREPARE);
        this.setFilePrepare(str, OUTPUT);
        this.setFileIgnoreChar(str);
        String[] file = this.setDirectory(str + "/input");
        return file;
    }

    protected String[] setDirectory(String str) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.list();
    }

    protected void setFilePrepare(String str, String sub) {

        File filepre = new File(str + sub);
        if (!filepre.exists()) {
            filepre.mkdir();
        } else {
            clearFilePrepare(filepre);
        }
    }

    protected void setFileIgnoreChar(String str) {

        File filepre = new File(str + CONFIG);
        if (!filepre.exists()) {
            filepre.mkdir();
            writeIgnoeFileRemovetxt(str + FILEREMOVE);
            writeIgnoeFileRemovetxt(str + CSVTRANSLATE);
        } else {

        }
    }

    protected void clearFilePrepare(File filepre) {
        String[] entries = filepre.list();
        for (String s : entries) {
            File currentFile = new File(filepre.getPath(), s);
            currentFile.delete();
        }
    }

    protected void printFile(String[] list) {
        for (String file : list) {
            System.out.println(file);
        }
    }

    protected String getFile(String fileName) {
        StringBuilder strBuilder = new StringBuilder();
        try {
            BufferedReader in = reader(fileName);
            String str;
            while ((str = in.readLine()) != null) {
                strBuilder.append(str).append("\n");
            }
        } catch (Exception e) {
        }
        return strBuilder.toString();
    }

    protected BufferedReader reader(String fileName) throws Exception {

        FileInputStream stream = new FileInputStream(fileName);
        InputStreamReader input = new InputStreamReader(stream, "UTF-8");
        BufferedReader in = new BufferedReader(input);
        return in;
    }

    protected void writeIgnoeFileSpacetxt(String path) {
        String data = "";
        this.writeUsingFiles(data, path);

    }

    protected void writeIgnoeFileRemovetxt(String path) {
        String data = "";
        this.writeUsingFiles(data, path);

    }

    protected void writeUsingFiles(String data, String path) {
        try {
            Files.write(Paths.get(path), data.getBytes());
        } catch (IOException e) {
        }
    }

}
