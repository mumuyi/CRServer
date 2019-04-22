package cn.nuaa.search;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class MyFormat {
	public static List<String> VariableDeclarationList = new ArrayList<String>();
	public static List<String> VariableNameList = new ArrayList<String>();
	public static List<String> VariableTypeList = new ArrayList<String>();
	public static Map<String, Integer> TypeMap = new HashMap<String, Integer>();
	public static MethodDeclarationEntity methodDeclaration = new MethodDeclarationEntity();

	private static final Set<String> BasicTypeSet = new HashSet<String>() {
		private static final long serialVersionUID = -7711952209080770726L;

		{
			add("int");
			add("long");
			add("short");
			add("float");
			add("double");
			add("String");
			add("byte");
			add("char");
			add("boolean");
			add("int[]");
			add("long[]");
			add("short[]");
			add("float[]");
			add("double[]");
			add("String[]");
			add("byte[]");
			add("char[]");
			add("boolean[]");
		}
	};

	private static String fileName = "Activiti-develop@AbstractActivitiTestCase#assertAndEnsureCleanDb.txt";
	private static String filePath = "F:\\data\\github\\methodbody\\";

	public static void main(String[] args) {

		// 从文件中获取源码;并对其进行格式化;
		String code = codeFormat(addClassHead(readCodeFromFile("F:\\Java\\TestPlugins\\code\\0.txt")));
		// 从文件中获取源码;并对其进行格式化;
		//String code = codeFormat(addClassHead(readCodeFromFile("F:\\data\\github\\methodbody\\" + fileName)));

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(code);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		// 解析代码;获取变量声明信息;
		SingleFileTest(code);

		// 去掉源码中所有的变量声明;
		String nCode = removeVariableDeclaration(code);
		String oCode = nCode;

		// 将变量替换为其原有类型;
		nCode = replaceVariableName(oCode);
		oCode = nCode;

		// 去掉类结构;
		nCode = removeClassHead(nCode);
		oCode = nCode;

		// 信息输出;
		System.out.println("Format Code:");
		System.out.println(nCode);

		System.out.println("\nVariable Declaration:");
		for (String i : VariableDeclarationList) {
			System.out.println(i);
		}

		System.out.println("\nVariable Type Information:");
		for (String s : TypeMap.keySet()) {
			System.out.println("type: " + s + "      num: " + TypeMap.get(s));
		}

		System.out.println("\nMethod Declaration:");
		System.out.println("method name: " + methodDeclaration.getMethodName());
		System.out.println("method return value: " + methodDeclaration.getMethodRetureType());
		for (String s : methodDeclaration.getMethodParameters()) {
			System.out.println("parameter: " + s);
		}

		// 存储数据;
		// storeData(nCode);

		// 清理数据;
		// clearData();

		// 读取数据;
		// readData();

		// formatCode();
		// removeSpaceLine();
	}

	public static void singleFormat(String oldCode){
		// 从文件中获取源码;并对其进行格式化;
		String code = codeFormat(addClassHead(oldCode));
		// 从文件中获取源码;并对其进行格式化;
		//String code = codeFormat(addClassHead(readCodeFromFile("F:\\data\\github\\methodbody\\" + fileName)));

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(code);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		// 解析代码;获取变量声明信息;
		SingleFileTest(code);

		// 去掉源码中所有的变量声明;
		String nCode = removeVariableDeclaration(code);
		String oCode = nCode;

		// 将变量替换为其原有类型;
		nCode = replaceVariableName(oCode);
		oCode = nCode;

		// 去掉类结构;
		nCode = removeClassHead(nCode);
		oCode = nCode;

		// 信息输出;
		System.out.println("Format Code:");
		System.out.println(nCode);

		System.out.println("\nVariable Declaration:");
		for (String i : VariableDeclarationList) {
			System.out.println(i);
		}

		System.out.println("\nVariable Type Information:");
		for (String s : TypeMap.keySet()) {
			System.out.println("type: " + s + "      num: " + TypeMap.get(s));
		}

		System.out.println("\nMethod Declaration:");
		System.out.println("method name: " + methodDeclaration.getMethodName());
		System.out.println("method return value: " + methodDeclaration.getMethodRetureType());
		for (String s : methodDeclaration.getMethodParameters()) {
			System.out.println("parameter: " + s);
		}

		// 存储数据;
		// storeData(nCode);

		// 清理数据;
		// clearData();

		// 读取数据;
		// readData();

		// formatCode();
		// removeSpaceLine();
	}
	
	
	
	public static void formatCode() {
		File directory = new File(filePath);
		File[] insFiles = directory.listFiles();
		int count = 0;
		for (int i = 100000; i < insFiles.length; i++) {
			fileName = insFiles[i].getName();
			System.out.println(fileName);
			// 从文件中获取源码;并对其进行格式化;
			String code = codeFormat(addClassHead(readCodeFromFile("F:\\data\\github\\methodbody\\" + fileName)));

			if (code.contains(" abstract ")) {
				count++;
				continue;
			}

			// System.out.println(code);
			// 解析代码;获取变量声明信息;
			SingleFileTest(code);

			// 去掉源码中所有的变量声明;
			String nCode = removeVariableDeclaration(code);
			String oCode = nCode;

			// 将变量替换为其原有类型;
			nCode = replaceVariableName(oCode);
			oCode = nCode;

			// 去掉类结构;
			nCode = removeClassHead(nCode);
			oCode = nCode;

			// 存储数据;
			storeData(nCode);

			// 清理数据;
			clearData();

			if (i > 100200)
				break;
		}
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + count);
	}

	/**
	 * 清理数据;
	 */
	private static void clearData() {
		VariableDeclarationList.clear();
		VariableNameList.clear();
		VariableTypeList.clear();
		TypeMap.clear();
		methodDeclaration.getMethodParameters().clear();
	}

	/**
	 * 去掉代码段中的所有声明; 或者说将声明和方法体分开;
	 */
	private static String removeVariableDeclaration(String code) {
		String oCode = code;
		String nCode = null;
		for (String i : VariableDeclarationList) {
			nCode = oCode.replace((i.replace("=", " = ").replace("\n", "")), "\n");
			oCode = nCode;
		}

		nCode = oCode.replaceAll(" +\n", "");

		// System.out.println(nCode);
		return nCode;
	}

	/**
	 * 将代码段中的变量替换为其原有类型;
	 */
	private static String replaceVariableName(String code) {
		String nCode = code;
		String oCode = code;
		for (int i = 0; i < VariableTypeList.size(); i++) {
			if (BasicTypeSet.contains(VariableTypeList.get(i))) {
				continue;
			}

			// nCode =
			// oCode.replaceAll(VariableNameList.get(i),VariableTypeList.get(i));
			// oCode = nCode;

			nCode = oCode.replaceAll(VariableNameList.get(i) + " ", VariableTypeList.get(i) + " ");
			oCode = nCode;
			nCode = oCode.replaceAll(VariableNameList.get(i) + "\\.", VariableTypeList.get(i) + "\\.");
			oCode = nCode;
			nCode = oCode.replaceAll(VariableNameList.get(i) + ",", VariableTypeList.get(i) + ",");
			oCode = nCode;
			nCode = oCode.replaceAll(VariableNameList.get(i) + ";", VariableTypeList.get(i) + ";");
			oCode = nCode;
			nCode = oCode.replaceAll(VariableNameList.get(i) + "\\)", VariableTypeList.get(i) + ")");
			oCode = nCode;
		}

		for (int i = 0; i < methodDeclaration.getMethodParameters().size(); i++) {
			String[] types = methodDeclaration.getMethodParameters().get(i).split(" ");
			int f = types.length - 2;
			int e = types.length - 1;
			if (BasicTypeSet.contains(types[f])) {
				continue;
			}

			// nCode =
			// oCode.replaceAll(VariableNameList.get(i),VariableTypeList.get(i));
			// oCode = nCode;
			nCode = oCode.replaceAll(types[e] + " ", types[f] + " ");
			oCode = nCode;
			nCode = oCode.replaceAll(types[e] + "\\.", types[f] + "\\.");
			oCode = nCode;
			nCode = oCode.replaceAll(types[e] + ",", types[f] + ",");
			oCode = nCode;
			nCode = oCode.replaceAll(types[e] + ";", types[f] + ";");
			oCode = nCode;
			nCode = oCode.replaceAll(types[e] + "\\)", types[f] + "\\)");
			oCode = nCode;
		}

		return nCode;
	}

	/**
	 * 解析代码; 现在需要将带解析的方法体包含在一个类的结构里面;
	 */
	private static void SingleFileTest(String code) {
		CompilationUnit comp = getCompilationUnit(code);
		MyCodeVisitor visitor = new MyCodeVisitor();
		comp.accept(visitor);
		// 获取import数据;
		// System.out.println(comp.imports());
		// 返回了整个类,暂时不知道干嘛的;
		// System.out.println(comp.types());
	}

	/**
	 * 由代码字符串直接生成CompilationUnit;
	 */
	public static CompilationUnit getCompilationUnit(String code) {
		ASTParser astParser = ASTParser.newParser(AST.JLS3);
		astParser.setSource(code.toCharArray());
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);

		CompilationUnit result = (CompilationUnit) (astParser.createAST(null));

		return result;
	}

	/**
	 * 从文件中直接读取数据,并返回CompilationUnit;
	 */
	public static CompilationUnit getCompilationUnitFromFile(String javaFilePath) {
		byte[] input = null;
		try {
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(javaFilePath));
			input = new byte[bufferedInputStream.available()];
			bufferedInputStream.read(input);
			bufferedInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ASTParser astParser = ASTParser.newParser(AST.JLS3);
		astParser.setSource(new String(input).toCharArray());
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);

		CompilationUnit result = (CompilationUnit) (astParser.createAST(null));

		return result;
	}

	/**
	 * 格式化源代码;
	 */
	private static String codeFormat(String code) {
		@SuppressWarnings("unchecked")
		Map<String, String> options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		// 读取配置;
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
		// 一行多少个字符后会换行;
		options.replace("org.eclipse.jdt.core.formatter.lineSplit", "2000");
		// 制表符;
		options.replace("org.eclipse.jdt.core.formatter.tabulation.char", "space");
		// 去除空行;
		options.replace("org.eclipse.jdt.core.formatter.comment.clear_blank_lines_in_block_comment", "true");
		options.replace("org.eclipse.jdt.core.formatter.comment.clear_blank_lines_in_javadoc_comment  ", "true");

		CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(options);
		TextEdit textEdit = codeFormatter.format(CodeFormatter.F_INCLUDE_COMMENTS, code, 0, code.length(), 0, null);
		IDocument doc = new Document(code);
		try {
			textEdit.apply(doc);
			// System.out.println(doc.get());
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		// System.out.println(doc.get());
		return doc.get();
	}

	/**
	 * 添加类结构;
	 */
	private static String addClassHead(String code) {
		// System.out.println("public class test{" + code + "}");
		return "public class test{" + code + "}";
	}

	/**
	 * 删除类结构;
	 */
	private static String removeClassHead(String code) {
		// System.out.println("public class test{" + code + "}");
		// System.out.println(code);
		String nCode = code.replace("public class test {", "");
		return nCode.substring(0, nCode.length() - 1);
	}

	/**
	 * 从文件中读取代码;
	 */
	private static String readCodeFromFile(String path) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; // 用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		String code = "";
		try {
			String str = "";
			fis = new FileInputStream(path);// FileInputStream
			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
			br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new
											// InputStreamReader的对象
			while ((str = br.readLine()) != null) {
				// 因为Format方法不能处理List<String> list = new ArrarList<String>();
				// 中的<String>这种写法;
				// 因此在这里去掉这部分内容;
				if (str == null || str.equals("\n") || str.equals("")) {
					continue;
				}
				code += (str);
			}
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// System.out.println(code);
		return code;
	}

	/**
	 * 存储解析的数据;
	 */
	private static void storeData(String code) {
		// 存储变量声明;
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < VariableDeclarationList.size(); i++) {
			str.append(VariableDeclarationList.get(i));
		}
		try {
			if (str.length() > 1) {
				writeFileContent("F:\\data\\github\\methodVaribleDeclaration\\" + fileName,
						new StringBuffer(str.substring(0, str.length() - 1)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 存储格式化后的方法体;
		str = new StringBuffer(code);
		try {
			writeFileContent("F:\\data\\github\\methodFormatBody\\" + fileName, new StringBuffer(str));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 存储变量信息;
		str = new StringBuffer();
		for (String s : TypeMap.keySet()) {
			str.append(s + " " + TypeMap.get(s) + "\n");
		}
		try {
			if (str.length() > 1) {
				writeFileContent("F:\\data\\github\\methodVaribleDeclarationInformation\\" + fileName,
						new StringBuffer(str.substring(0, str.length() - 1)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 存储方法信息;
		File file = new File("F:\\data\\github\\methodBasicInformation\\" + fileName);
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(methodDeclaration);
			objOut.flush();
			objOut.close();
			// System.out.println("write object success!");
		} catch (IOException e) {
			System.out.println("write object failed");
			e.printStackTrace();
		}
	}

	/**
	 * 写入文件;
	 */
	private static boolean writeFileContent(String filepath, StringBuffer buffer) throws IOException {
		Boolean bool = false;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			File file = new File(filepath);// 文件路径(包括文件名称)

			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			pw.write(buffer.toString().toCharArray());
			pw.flush();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 不要忘记关闭
			if (pw != null) {
				pw.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return bool;
	}

	/**
	 * 去掉代码中的空白行;
	 */
	public static void removeSpaceLine() {
		File directory = new File("F:\\data\\github\\methodFormatBody\\");
		File[] insFiles = directory.listFiles();
		System.out.println(insFiles.length);
		int line = 0;
		StringBuffer strbuf = new StringBuffer();
		for (int i = 0; i < 1; i++) {

			String str = "";
			fileName = insFiles[i].getName();
			try {
				FileReader fr = new FileReader("F:\\data\\github\\methodFormatBody\\" + fileName);
				BufferedReader br = new BufferedReader(fr);
				System.out.println(fileName);
				while ((str = br.readLine()) != null) {
					if (str != null && !str.equals("")) {
						if (str.startsWith(" ")) {
							strbuf.append(str);
							strbuf.append("\n");
							line++;
						}
					}
				}
				fr.close();
				br.close();

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		System.out.println(line);
		System.out.println(strbuf.toString());
		strbuf.deleteCharAt(strbuf.length() - 1);
		try {
			writeFileContent("C:\\Users\\ai\\Desktop\\1.txt", strbuf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readData(String fileName) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; // 用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		try {
			String str = "";
			fis = new FileInputStream("F:\\data\\github\\methodVaribleDeclaration\\" + fileName);// FileInputStream
			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
			br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new
											// InputStreamReader的对象
			while ((str = br.readLine()) != null) {
				VariableDeclarationList.add(str);
			}
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			String str = "";
			fis = new FileInputStream("F:\\data\\github\\methodVaribleDeclarationInformation\\" + fileName);// FileInputStream
			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
			br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new
											// InputStreamReader的对象
			while ((str = br.readLine()) != null) {
				String[] strs = str.split(" ");
				TypeMap.put(strs[0], Integer.parseInt(strs[1]));
			}
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/*
		 * File file = new File("F:\\data\\github\\methodBasicInformation\\" +
		 * fileName); FileInputStream in; try { in = new FileInputStream(file);
		 * ObjectInputStream objIn = new ObjectInputStream(in);
		 * methodDeclaration = (MethodDeclarationEntity) objIn.readObject();
		 * objIn.close(); // System.out.println("read object success!"); } catch
		 * (IOException e) { System.out.println("read object failed");
		 * e.printStackTrace(); } catch (ClassNotFoundException e) {
		 * e.printStackTrace(); }
		 */

		// 信息输出;

		System.out.println("\nVariable Declaration:");
		for (String i : VariableDeclarationList) {
			System.out.println(i);
		}

		System.out.println("\nVariable Type Information:");
		for (String s : TypeMap.keySet()) {
			System.out.println("type: " + s + "      num: " + TypeMap.get(s));
		}

		/*
		 * System.out.println("\nMethod Declaration:"); System.out.println(
		 * "method name: " + methodDeclaration.getMethodName());
		 * System.out.println("method return value: " +
		 * methodDeclaration.getMethodRetureType()); for (String s :
		 * methodDeclaration.getMethodParameters()) { System.out.println(
		 * "parameter: " + s); }
		 */
	}
}
