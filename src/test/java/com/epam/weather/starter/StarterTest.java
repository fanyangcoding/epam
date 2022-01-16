package com.epam.weather.starter;

import com.example.starter.MyStarter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StarterTest {

    @Autowired
    private MyStarter myStarter;

    @Test
    public void testStarter() {
        System.out.println(myStarter.show());
    }
}
