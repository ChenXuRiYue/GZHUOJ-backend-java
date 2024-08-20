package common.bloom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.util.ArrayList;
import java.util.BitSet;

@Component
public class Bloom {
    @Autowired
    RedisTemplate redisTemplate;

    final Integer MAXX=200000;
    MyHash myHash[];

    public Bloom(){
        myHash=new MyHash[3];
        myHash[0]=new MyHash(133331,33);
        myHash[1]=new MyHash(177771,15);
        myHash[2]=new MyHash(199921,713333);
    }

    public boolean probablyHave(Object a,String b) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(b))) {
            //System.out.printf(String.valueOf(redisTemplate.hasKey(b)));
            BitSet bloom = (BitSet)redisTemplate.opsForValue().get(b);
            for (MyHash hash : myHash) {
                int hashZhi = hash.hash(a);
                if (!bloom.get(hashZhi)) {
                    return false;
                }
            }
            return true;
        }else {
            return false;
        }
    }
    public void add(Object a,String b) {
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(b))){
            BitSet bitSet = new BitSet(MAXX);
            redisTemplate.opsForValue().set(b,bitSet);
        }

        BitSet bloom = (BitSet)redisTemplate.opsForValue().get(b);

        for (MyHash hash : myHash) {
            int hashZhi = hash.hash(a);
            bloom.set(hashZhi,true);
        }
        redisTemplate.opsForValue().set(b,bloom);
        //这种更改方式效率较低，以后需要更新为直接更改redis里bitset的数据
    }
    class MyHash {
        private int mos;
        private int seed;

        MyHash(int mos, int seed) {
            this.mos = mos;
            this.seed = seed;
        }

        int hash(Object obj) {
            int hashCode = Math.abs(obj.hashCode());
            return (hashCode+seed)%mos;
        }
    }

    public static void main(String[] args) {
        //for Test
    }
}
