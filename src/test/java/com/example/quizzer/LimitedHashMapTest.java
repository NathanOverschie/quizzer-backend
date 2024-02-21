package com.example.quizzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LimitedHashMapTest {

    private final int maxSize = 10;
    private LimitedHashMap<Integer, Integer> map;

    private int id = 0;

    @BeforeEach
    void init(){
        map = new LimitedHashMap<>(maxSize);
    }



    private void fullMap(){
        map.clear();

        for(int i = 0; i < maxSize ; i++){
            map.put(uniqueInt(), uniqueInt());
        }
    }

    private int uniqueInt() {
        return id++;
    }

    @Test
    void fullMapDoesNotGrowWithPut(){
        //Setup
        fullMap();

        //Act
        map.put(uniqueInt(), uniqueInt());

        //Assert
        assertEquals(maxSize, map.size());
    }


    @Test
    void fullMapDoesNotGrowWithPutAll(){
        //Setup
        fullMap();

        //Act
        HashMap<Integer, Integer> m = new HashMap<>();
        m.put(uniqueInt(), uniqueInt());
        m.put(uniqueInt(), uniqueInt());
        map.putAll(m);

        //Assert
        assertEquals(maxSize, map.size());
    }

    @Test
    void putAfterRemove(){
        //Setup
        fullMap();

        //Act
        map.remove(map.keySet().stream().findFirst().get());
        map.put(uniqueInt(), uniqueInt());
        map.put(uniqueInt(), uniqueInt());

        //Assert
        assertEquals(maxSize, map.size());
    }

    @Test
    void putAfterClear(){
        //Setup
        fullMap();

        //Act
        map.clear();

        fullMap();

        map.put(uniqueInt(), uniqueInt());

        //Assert
        assertEquals(maxSize, map.size());
    }

    @Test
    void mapHasLatestElements(){
        fullMap();

        List<Integer> latestKeys = new ArrayList<>();

        for(int i = 0; i < maxSize; i++){
            int unique = uniqueInt();
            latestKeys.add(unique);
            map.put(unique, uniqueInt());
        }

        for(Integer key : latestKeys){
            assertTrue(map.containsKey(key));
        }
    }
}