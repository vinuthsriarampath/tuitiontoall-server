/*
 * Copyright (c) 2026 vinuth sri arampath
 *
 * This code is the intellectual property of vinuth sri arampath and is protected under copyright law.
 * Unauthorized copying, modification, distribution, or use of this code, in whole or in part,
 * without prior written permission is strictly prohibited.
 *
 * Portions of this code may be generated with AI and modified by vinuth sri arampath
 * All rights reserved.
 *
 *
 */

package edu.vinu.domain.grading.validator;

import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.domain.grading.request.GradingRangeRequest;
import edu.vinu.common.dto.FieldError;
import edu.vinu.domain.grading.response.IndexedGradingRange;

import java.util.*;
import java.util.stream.IntStream;

public class GradingRangeValidator {
    public static void validate(List<? extends GradingRangeRequest> ranges, Integer totalMarks){
        ArrayList<FieldError> errors = new ArrayList<>();
        Set<String> grades = new HashSet<>();

        if(ranges == null || ranges.isEmpty()){
            throw new InvalidInputException("At least one grading range is required!");
        }

        List<IndexedGradingRange> sortedRanges =
                IntStream.range(0, ranges.size())
                        .mapToObj(i -> new IndexedGradingRange(i, ranges.get(i)))
                        .sorted(Comparator.comparing(
                                item -> item.range().minMarks()
                        ))
                        .toList();

        IndexedGradingRange first = sortedRanges.get(0);
        IndexedGradingRange last = sortedRanges.get(sortedRanges.size()-1);

        if(first.range().minMarks() != 0){
            errors.add(new FieldError(
                    "gradingRanges["+first.originalIndex()+"].minMarks",
                    "Minimum mark of the first grading range should be 0!"
            ));
        }

        if(!last.range().maxMarks().equals(totalMarks)){
            errors.add(new FieldError(
                    "gradingRanges["+last.originalIndex()+"].maxMarks",
                    "Maximum mark of the last grading range should be "+totalMarks+"!"
            ));
        }

        for (int i = 0; i < sortedRanges.size(); i++) {

            IndexedGradingRange currentIndexedGradingRange = sortedRanges.get(i);
            GradingRangeRequest currentGradingRange = sortedRanges.get(i).range();

            if (currentGradingRange.minMarks() < 0 || currentGradingRange.minMarks() >= currentGradingRange.maxMarks() || currentGradingRange.minMarks() > totalMarks) {
                errors.add(new FieldError(
                        "gradingRanges[" + currentIndexedGradingRange.originalIndex() + "].minMarks",
                        "Minimum mark is invalid! Minimum marks should be greater than or equal to 0, less than maximum mark and total marks of the assignment!"
                ));
            }

            if (currentGradingRange.maxMarks() <= 0 || currentGradingRange.maxMarks() <= currentGradingRange.minMarks() || currentGradingRange.maxMarks() > totalMarks) {
                errors.add(new FieldError(
                        "gradingRanges[" + currentIndexedGradingRange.originalIndex() + "].maxMarks",
                        "Maximum mark is invalid! Maximum marks should be greater than 0, greater than minimum mark and less than or equal to total marks of the assignment!"
                ));
            }

            if(!grades.add(currentGradingRange.desiredGrade())){
                errors.add(new FieldError(
                        "gradingRanges[" + currentIndexedGradingRange.originalIndex() + "].desiredGrade",
                        "Cannot have duplicated grades!"
                ));
            }

            if (i > 0) {
                IndexedGradingRange previousIndexedGradingRange = sortedRanges.get(i - 1);

                if (currentGradingRange.minMarks() <= previousIndexedGradingRange.range().maxMarks()) {
                    errors.add(new FieldError(
                            "gradingRanges[" + currentIndexedGradingRange.originalIndex() + "].minMarks",
                            "Minimum mark conflicts with previous max marks!"
                    ));
                }

                if(currentGradingRange.minMarks()-previousIndexedGradingRange.range().maxMarks() > 1){
                    errors.add(new FieldError(
                            "gradingRanges[" + currentIndexedGradingRange.originalIndex() + "].minMarks",
                            "There is a gap between previous max marks and current min marks!"
                    ));
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }
    }
}
