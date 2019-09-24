package com.ads.adserver.utils;

import com.ads.adserver.entities.AdEntity;
import com.ads.adserver.entities.Click;
import com.ads.adserver.entities.Impression;
import com.ads.adserver.service.AdService;
import com.ads.adserver.service.ClickService;
import com.ads.adserver.service.ImpressionService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class AdSelectionUtil {

    @Resource
    AdService adService;

    @Resource
    ImpressionService impService;

    @Resource
    ClickService clickService;

    public AdSelectionUtil(AdService adService, ImpressionService impressionService, ClickService clickService) {
        this.adService = adService;
        this.impService = impressionService;
        this.clickService = clickService;
    }

    public static final int numLookbackDays = 7;

    // Algo Weights
    public static final double impPercWeight = 0.30;
    public static final double clickPercWeight = 0.30;
    public static final double clickCTRWeight = 0.40;

    public enum AdTierWeights {

        tier1(0.60),
        tier2(0.25),
        tier3(0.10),
        tier4(0.05);


        private double value;


        public double getValue() {
            return this.value;
        }


        AdTierWeights(double value) {
            this.value = value;
        }
    }

    public AdEntity getBestMatchDefault() {

        List<Impression> allImps = impService.getAllImpressionWithinInterval(numLookbackDays);
        List<Click> allClicks = clickService.getAllClicksWithinInterval(numLookbackDays);
        List<AdEntity> allAds = adService.findAll();


        // List<AdEntity> allAds = adService.findAll();

        /**
         * The Algo is based on adScore which is calculated dynamically each time.
         *
         * adScore for each ad is based on
         * 1. Distribution of impressions (distImp),
         *    no of Impressions for an ad / total no of impressions (over n days)
         * 2. Distribution of clicks (distClick),
         *    no of Clicks for an ad / total no of clicks (over n days)
         * 3. Click through rate for an ad (clickCTR),
         *    No of clicks for ad / total no of impressions (over n days).
         *
         * And Custom weights for impression distribution
         * 1. impPercWeight (how much weight to give to impression data in score)
         * 2. clickPercWeight (how much weight to give to click data in score)
         * 3. clickCTRWeight (how much weight to give to CTR data in score)
         * 4. tierWeight (Higher tier means high freq ad as users paid more)
         *
         * adScore = (distImp*impPercWeight*tierWeight) + (distClick*clickPercWeight) + (clickCTR*clickCTRWeight)
         */

        /** Counting Impressions and Clicks. can be done via SQL too but doing it this way for flexibility
         *
         */

        Map<Integer, Integer> distImpAll = new HashMap<>();
        Map<Integer, Integer> distClickAll = new HashMap<>();

        Map<AdEntity, Double> adScoreMap = new HashMap<>();

        allImps.forEach(adImp -> {
            if (!distImpAll.containsKey(adImp.getAdId())) {
                distImpAll.put(adImp.getAdId(), 1);
            } else {
                int count = distImpAll.get(adImp.getAdId());
                distImpAll.put(adImp.getAdId(), count + 1);
            }
        });
        System.out.println("AdId\tCount");
        distImpAll.forEach((adId, count) -> {
            System.out.println(adId + "\t" + count);

        });


        allClicks.forEach(adClick -> {
            if (!distClickAll.containsKey(adClick.getAdId())) {
                distClickAll.put(adClick.getAdId(), 1);
            } else {
                int count = distClickAll.get(adClick.getAdId());
                distClickAll.put(adClick.getAdId(), count + 1);
            }
        });

        System.out.println("AdId\tCount");
        distClickAll.forEach((adId, count) -> {
            System.out.println(adId + "\t" + count);

        });

        int totalImpressions = allImps.size();
        System.out.println("Total Impressions: " + totalImpressions);
        int totalClicks = allClicks.size();
        System.out.println("Total Clicks: " + totalClicks);
        System.out.println("Default Weihts are ");
        System.out.println("impPercWeight: " + impPercWeight);
        System.out.println("clickPercWeight: " + clickPercWeight);
        System.out.println("clickCTRWeight: " + clickCTRWeight);
        for (AdEntity ad : allAds) {
            System.out.println("Calculating Score for adId: " + ad.getId());
            if (distClickAll.containsKey(ad.getId()) || distImpAll.containsKey(ad.getId())) {
                System.out.println("Data Exists for adId: " + ad.getId());


                int distImp = distImpAll.getOrDefault(ad.getId(), 0);
                int distClick = distClickAll.getOrDefault(ad.getId(), 0);
                double tierWeight = getTierRate(ad.getCatId());

                System.out.println("Impression Dist: " + distImp);
                System.out.println("Click Dist: " + distClick);
                double adScore =
                        (((double) distImp / totalImpressions) * impPercWeight * tierWeight) * 100 +
                                (((double) distClick / totalClicks) * clickPercWeight) * 100 +
                                (((double) distClick / totalImpressions) * clickCTRWeight) * 100;
                System.out.println("Calculated score is " + adScore);
                adScoreMap.put(ad, adScore);
            } else {
                System.out.println("Data DO NOT Exists for adId: " + ad.getId());
                System.out.println("Using Default Score 0");
                adScoreMap.put(ad, 0.0);
            }
        }


        adScoreMap = adScoreMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        System.out.println("AdId\tAdScore");
        System.out.println("----\t-------");

        adScoreMap.forEach((adId, adScore) -> {
            System.out.println(adId.getId() + "\t" + adScore);

        });
        AdEntity selectedAd = adScoreMap.entrySet().iterator().next().getKey();
        Impression newImpression = new Impression();
        newImpression.setAdCat(selectedAd.getCatId());
        newImpression.setAdId(selectedAd.getId());
        newImpression.setrLogId(1000);
        impService.createImpression(newImpression);
        return selectedAd;
    }

    private double getTierRate(int tierId) {
        switch (tierId) {
            case 1:
                return AdTierWeights.tier1.getValue();
            case 2:
                return AdTierWeights.tier2.getValue();
            case 3:
                return AdTierWeights.tier3.getValue();
            case 4:
                return AdTierWeights.tier4.getValue();
            default:
                return AdTierWeights.tier3.getValue();
        }
    }
}

class CustomComparator<K, V extends Comparable> implements Comparator<K> {
    private Map<K, V> map;

    public CustomComparator(Map<K, V> map) {
        this.map = new HashMap<>(map);
    }

    @Override
    public int compare(K s1, K s2) {
        return map.get(s1).compareTo(map.get(s2));
    }
}
