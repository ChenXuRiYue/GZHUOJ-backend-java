import com.gzhuoj.contest.ContestApplication;
import com.gzhuoj.contest.mapper.ContestProblemMapper;
import common.bloom.Bloom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = ContestApplication.class)
public class test {
    @Autowired
    ContestProblemMapper contestProblemMapper;

    /*
    @Test
    public void test1() {
        SFC sfc = new SFC();
        sfc.contestId=13;
        sfc.problemId=18;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        String dateString = "2024-08-03 00:00:01";
        sfc.beginTime = sdf.parse(dateString);
        dateString = "2024-08-08 00:00:01";
        sfc.endTime = sdf.parse(dateString);

        sfc.status=1;
        //System.out.println("结果："+contestProblemMapper.selectForContest(sfc));
        System.out.println(contestProblemMapper.selectByProblemId(18,13));
    }
    */
    @Autowired
    Bloom bloom;
    /*
    @Test
    public void test2() {

        int a=1,b=2,c=3,d=4,e=5,f=6,g=1,h=1;
        System.out.println(bloom.probablyHave(a,"123"));
        bloom.add(a,"123");
        System.out.println(bloom.probablyHave(a,"123"));
        System.out.println(bloom.probablyHave(b,"123"));
        System.out.println(bloom.probablyHave(c,"123"));
        System.out.println(bloom.probablyHave(g,"123"));
    }*/
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
