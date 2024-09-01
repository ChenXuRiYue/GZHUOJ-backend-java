# GZHUOJ-backend-java
广州大学ACM算法竞赛集训队开发的在线评测系统

项目选型： SpringCloud + SpringBoot + MySQL + Mybatis + Redis + Nginx

### 项目概况：
<br/>
- 微服务架构设计，各模块功能逻辑独立，支持赛时伸缩扩展评测机<br/>
- 基于 Spring Cloud 和 consul 完成轻量化，跨语⾔的微服务治理。<br/>
- 实现评测服务调度负载均衡、多任务提交、多线程评测实现赛时高并发评测。<br/>
- 基于Go-Judge 评测沙箱编译运行代码<br/>
- 比赛服务多样性，支持多语言、多评测模式、多赛制<br/>
- 集成用户管理、比赛模块、题目模块、气球派发、代码打印、座位生成、工作分配、滚榜、获奖派发等功能。<br/>
- 容器级交付，公有云或物理机，使用Docker和Docker-Compose进行服务编排与部署，实现一键化部署<br/>


### 部署：
当前项目仍在持续迭代更新中，未给出稳定版本，主分支代码可能随时改变
<br/><br/>

**推荐部署环境**： Ubuntu 22.04.4 LTS
一键化部署
```
git clone https://github.com/ChenXuRiYue/GZHUOJ-backend-java.git
cd GZHUOJ-backend-java
chmod u+x build.sh
./build.sh
```


### 主要开发成员
<table>
    <tr>
        <td align="center" style="width: 33%;">
            <img src="https://github.com/ChenXuRiYue.png?s=64" width="100" height="100" />
            <br />
            <a href="https://github.com/ChenXuRiYue" target="_blank">ChenXuRiYue</a>
            <br />
            <strong> Initiator, Backend, Frontend </strong>
        </td>
        <td align="center" style="width: 33%;">
            <img src="https://github.com/LJCzzzzZ.png?s=64" width="100" height="100" />
            <br />
            <a href="https://github.com/LJCzzzzZ" target="_blank">LJCzzzzZ</a>
            <br />
            <strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Initiator, Backend&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </strong>
        </td>
        <td align="center" style="width: 33%;">
            <img src="https://github.com/sldpzshdwz.png?s=64" width="100" height="100" />
            <br />
            <a href="https://github.com/sldpzshdwz" target="_blank">sldpzshdwz</a>
            <br />
            <strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Backend&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </strong>
        </td>
    </tr>
    <tr>
        <td align="center" style="width: 33%;">
            <img src="https://github.com/KawaiiNahida.png?s=64" width="100" height="100" />
            <br />
            <a href="https://github.com/KawaiiNahida" target="_blank">KawaiiNahida</a>
            <br />
            <strong> Go-judge Owner </strong>
        </td>
        <td align="center" style="width: 33%;">
            <img src="https://github.com/Mikeklklkl.png?s=64" width="100" height="100" />
            <br />
            <a href="https://github.com/Mikeklklkl" target="_blank">Mikeklklkl</a>
            <br />
            <strong> Frontend </strong>
        </td>
    </tr>
</table>



