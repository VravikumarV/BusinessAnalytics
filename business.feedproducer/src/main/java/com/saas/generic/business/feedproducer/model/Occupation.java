package com.saas.generic.business.feedproducer.model;


import lombok.*;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Occupation {

    private String occupationId;
    private String userProfileId;
    private String occupationName;
    private String occupationDesc;
    private String occupationFrom;
    private String occupationTo;
    private String occupationDuration;
    private String designation;         //  Autosuggestions
    private String organization;
    private String orgSector;           // Master Data
    private String salary;

}
