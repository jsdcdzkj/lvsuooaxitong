package com.jsdc.lsoa.controller;

import com.jsdc.lsoa.model.ClientInfo;
import com.jsdc.lsoa.vo.CaseInfoVo;
import com.jsdc.lsoa.vo.ResultInfo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class CaseInfoControllerTest {
    @Autowired
    private CaseInfoController caseInfoController;
    
    
    @Test
    void getPageList() {
        CaseInfoVo caseInfoVo = new CaseInfoVo();
        caseInfoVo.setCaseType("1");

        ResultInfo pageList = caseInfoController.getPageList(caseInfoVo);
        System.out.println(pageList);
    }

    @Test
    void saveCaseInfo() {
        CaseInfoVo vo = new CaseInfoVo();
        vo.setCaseName("徐州市云龙区“1死1伤重大刑事案件");
        vo.setCaseType("2");
        vo.setBusinessType("2");
        vo.setCaseDesc("罪犯刑某系铜山区人，2013年前后，刑某在婚姻存续期间与滕某（被害人，女，时年29岁）发生婚外情，2013年4月，刑某和前妻离婚后，因滕某家人反对及性格问题，二人分分合合，2015年，刑某两次翻墙进入滕某家中滋扰，殴打滕某及其家人，被法院以非法侵入住宅罪判处缓刑，2017年年中，滕某提出分手，但刑某纠缠不 久，滕某报警处理。");
        vo.setMoney("10000");
        vo.setStage("2");
        vo.setStatus("1");
        vo.setSubjectAmount("200");
        vo.setInvoice("1");
        vo.setLetter("1");
        List<ClientInfo> objects = new ArrayList<>();
        ClientInfo clientInfo1 = new ClientInfo();
        clientInfo1.setName("黄世仁");
        clientInfo1.setIsPerson("1");
        clientInfo1.setPhone("18888888888");
        clientInfo1.setCode("320305");
        ClientInfo clientInfo2 = new ClientInfo();
        clientInfo2.setName("jsdc");
        clientInfo2.setIsPerson("2");
        clientInfo2.setPhone("18888888889");
        clientInfo2.setCode("320305");
        objects.add(clientInfo1);
        objects.add(clientInfo2);
        vo.setClientInfoVoList(objects);
        caseInfoController.saveCaseInfo(vo);
    }

    @Test
    void updateCaseInfo() {
        CaseInfoVo vo = new CaseInfoVo();
        vo.setId(4);
        vo.setMoney("3000");
        caseInfoController.updateCaseInfo(vo);
    }

    @Test
    void updateClientInfo() {
    }

    @Test
    void updateFile() {
    }

    @Test
    void getEntity() {
        CaseInfoVo vo = new CaseInfoVo();
        vo.setId(10);
        caseInfoController.getEntity(vo);
    }
    @Test
    void del() {
        CaseInfoVo vo = new CaseInfoVo();
        vo.setId(4);
        caseInfoController.del(vo);
    }
}