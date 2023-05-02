package com.example.demo.filter;

import org.springframework.stereotype.Component;

@Component
public class AuditThreadLocal {

   public static final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

   public static void set(Integer value) {
       threadLocal.set(value);
   }

   public static Integer get(){
       return threadLocal.get();
   }

   public static void remove() {
       threadLocal.remove();
   }

}
