package com.example.demo.entity;

import lombok.Data;

/**
 * pa_seat_layout 表对象
 *
 * @author auto create
 */
@Data
public class PaSeatLayout {
    /**
     * int(11)
     */
    private int layoutId;
    /**
     * int(11)
     */
    private int seatCount;
    /**
     * varchar(20)
     */
    private String seatLayout;
    /**
     * int(11)
     */
    private int totalRow;
    /**
     * int(11)
     */
    private int row1;
    /**
     * int(11)
     */
    private int row2;
    /**
     * int(11)
     */
    private int row3;
    /**
     * int(11)
     */
    private int row4;
}
