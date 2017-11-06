package com.demo.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 *
 * POJO for communicating Error messages to Back-end Client API user
 *
 * @author Harinder Dang.
 *
 */

@Data
@AllArgsConstructor
public class ApiError {

    @NonNull
    String status;
    @NonNull
    String message;
}
