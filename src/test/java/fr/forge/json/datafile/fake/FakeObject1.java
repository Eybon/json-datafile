package fr.forge.json.datafile.fake;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FakeObject1 {

    private String field1;
    private int field2;
    private BigDecimal field3;
    private LocalDate field4;
    private LocalDateTime field5;

}
