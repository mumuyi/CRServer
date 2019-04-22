package cn.nuaa.cr;

import java.util.Calendar;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class PythonInvoking {
	public static void main(String[] args){
        Calendar calendar1 = Calendar.getInstance();
        long time1 = calendar1.getTimeInMillis();
		
        @SuppressWarnings("resource")
		PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("C:\\Users\\ai\\Desktop\\hello.py");

        PyFunction pyFunction = interpreter.get("hello", PyFunction.class); // 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
        PyObject pyObject = pyFunction.__call__(new PyInteger(1),new PyInteger(2)); // 调用函数

        System.out.println(pyObject);
        
        Calendar calendar2 = Calendar.getInstance();
        long time2 = calendar2.getTimeInMillis();
        System.out.println(time1);
        System.out.println(time2);
        System.out.println(time2 - time1);
	}
}
