package com.ads.adserver;


import com.ads.adserver.entities.AdEntity;
import com.ads.adserver.entities.Click;
import com.ads.adserver.entities.Impression;
import com.ads.adserver.service.AdService;
import com.ads.adserver.service.ClickService;
import com.ads.adserver.service.ImpressionService;
import com.ads.adserver.utils.AdSelectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;
import java.util.List;

@RestController
public class AdServerController {

    private static Logger log = LoggerFactory.getLogger(AdServerController.class);

    @Resource
    AdService adService;

    @Resource
    ClickService clickService;

    @Resource
    ImpressionService impressionService;

    /*@Autowired
    private ConcurrentUtils concurrentUtils;*/


    @RequestMapping("/test")
    public String test() {
        return "Greetings from Spring Boot!";
    }

    /*@RequestMapping("/showdata")
    public void showData() {
        AdEntity entity = new AdEntity(0, "test", 10002, 9355, "test@email.com");

        entity.printData();
    }*/

    /*@POST
    @RequestMapping("/createnewad")
    public Response.Status createNewAd(AdEntity ad){

    }*/

    @GetMapping(value = "/getallads")
    public List<AdEntity> getAllAds() {

        try {
            return adService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @PostMapping(value = "/createnewad")
    public Response.Status createNewAd(@RequestBody AdEntity ad) {

        try {
            adService.createAd(ad);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return Response.Status.OK;
    }

    @GetMapping(value = "/getad/{adid}")
    public AdEntity getAd(@PathVariable(value = "adid") final int id) {
        System.out.println("Fetching Adid " + id);
        return adService.getAd(id);
    }


    @PostMapping(value = "/registerclick")
    public Response.Status registerClick(@RequestBody(required = false) Click click) {
        try {
            clickService.createClick(click);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return Response.Status.OK;
    }


    @PostMapping(value = "/registerimpression")
    public Response.Status registerImpression(@RequestBody(required = false) Impression impression) {
        try {
            System.out.println(impression.getAdCat());
            impressionService.createImpression(impression);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.Status.BAD_REQUEST;
        }
        return Response.Status.OK;
    }

    @GetMapping(value = "/getallimpressions/{days}")
    public List<Impression> getAllImpressions(@PathVariable(value = "days") final int days) {

        try {
            return impressionService.getAllImpressionWithinInterval(days);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "/getallclicks/{days}")
    public List<Click> getAllClicks(@PathVariable(value = "days") final int days) {
        try {
            return clickService.getAllClicksWithinInterval(days);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @GetMapping(value = "/getbestmatchad")
    public synchronized AdEntity getBestMatchAd() {

        try {
            AdSelectionUtil adSelectionUtil = new AdSelectionUtil(adService, impressionService, clickService);
            return adSelectionUtil.getBestMatchDefault();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    @DeleteMapping(value = "/deletead/{adid}")
    public Response.Status deleteAd(@PathVariable(value = "adid") final int adid) {
        try {
            adService.deleteAd(adid);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.Status.BAD_REQUEST;
        }
        return Response.Status.OK;
    }

}
