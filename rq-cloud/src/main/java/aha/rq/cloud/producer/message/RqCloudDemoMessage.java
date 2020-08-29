package aha.rq.cloud.producer.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shoufeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RqCloudDemoMessage implements Serializable {
    private Long ahaId;
    private String ahaName;
    private String ahaValue;
}
