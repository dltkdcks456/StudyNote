package com.example.demo;

import com.example.demo.testA.AwithBImpl;
import com.example.demo.testA.ClassA;
import com.example.demo.testA.ClassAwithB;
import com.example.demo.testA.ClassB;
import com.example.demo.testB.ClassC;
import com.example.demo.testB.ClassD;
import com.example.demo.testB.ClassE;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void test() {
		ClassA classA = new AwithBImpl();
		classA.read();

		ClassB classB = new AwithBImpl();
		classB.write();

		ClassAwithB classAwithB = new AwithBImpl();
		classAwithB.read();
		classAwithB.write();
	}

	@Test
	void test1() {
		ClassC classD = new ClassD();
		classD.common();
		classD.read();

		ClassC classE = new ClassE();
		classE.common();
		classE.read();
	}
}
