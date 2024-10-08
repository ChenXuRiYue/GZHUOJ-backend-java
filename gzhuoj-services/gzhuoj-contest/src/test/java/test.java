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
        ContestProblemSubmissionsCalculateExample contestProblemSubmissionsCalculateExample = new ContestProblemSubmissionsCalculateExample();
        contestProblemSubmissionsCalculateExample.contestNum=13;
        contestProblemSubmissionsCalculateExample.problemNum=18;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        String dateString = "2024-08-03 00:00:01";
        contestProblemSubmissionsCalculateExample.beginTime = sdf.parse(dateString);
        dateString = "2024-08-08 00:00:01";
        contestProblemSubmissionsCalculateExample.endTime = sdf.parse(dateString);

        contestProblemSubmissionsCalculateExample.status=1;
        //System.out.println("结果："+contestProblemMapper.selectForContest(contestProblemSubmissionsCalculateExample));
        System.out.println(contestProblemMapper.selectByProblemNum(18,13));
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
