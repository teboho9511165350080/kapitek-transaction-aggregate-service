package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MerchantCategory {
    RENT("RENT"),
    ALCOHOL("Alcohol"),
    FOOD("Food"),
    FUEL("Fuel"),
    RETAIL("Retail"),
    UTILITIES("Utilities"),
    CLOTHING("Clothing"),
    AIRLINES("Airlines"),
    AUTOMOTIVE ("Automotive"),
    CAR_RENTAL ("Car Rental"),
    CONTRACTORS("Contractors"),
    INDUSTRIAL("Industrial"),
    DURABLE_GOODS ("Durable Goods"),
    PUBLISHING_PRINTING ("Publishing Printing"),
    NON_DURABLE_GOODS ("Non-durable Goods"),
    SPECIALTY_SERVICES ("Specialty Services"),
    DIGITAL_GOODS("Digital Goods"),
    FOOD_SERVICES("Food Services"),
    FINANCIAL_SERVICES("Financial Services"),
    TELECOMMUNICATIONS("Telecommunications"),
    ACCOMMODATION ("Accommodation"),
    TRANSPORTATION ("Transportation"),
    MEDICAL_EQUIPMENT("Medical Equipment"),
    OFFICE_SUPPLIES ("Office Supplies"),
    BUILDING_SUPPLIES ("Building Supplies"),
    BUSINESS_SERVICES("Business Service"),
    GOVERNMENT_SERVICES("Government Service"),
    MISCELLANEOUS_STORES("Miscellaneous Stores"),
    PROFESSIONAL_SERVICES("Professional Services"),
    RECREATION_ENTERTAINMENT("Recreation Entertainment"),
    AGRICULTURAL_SERVICES("Agriculture Services");

    private final String name;
}
