package cn.nuaa.search;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Prof {
	public static final String filePath = "E:\\data\\github\\";
	
	public static void main(String[] args) throws IOException{
		String oldPath = "F:\\Backup\\data\\github\\";
		String newPath = "E:\\data\\github\\";
		String name = "methodVaribleDeclarationInformation\\";
		File[] files = new File(oldPath + name).listFiles();
		for(int i = 0; i < 10000; i++){
			System.out.println(files[i].getName());
			copyFile(oldPath + name + files[i].getName(), newPath + name + files[i].getName());
		}
	}
	
	@SuppressWarnings("resource")
	public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);;

        //数据流（二进制）
        DataInputStream dis = null;
        DataOutputStream dos = null;
        dis = new DataInputStream(in);
        dos = new DataOutputStream(out);
        int i = 0;
        while((i = dis.read()) != -1){
            dos.write(i);
        }  
 }
}
