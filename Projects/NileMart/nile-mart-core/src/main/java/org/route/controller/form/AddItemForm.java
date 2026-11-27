
package org.route.controller.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddItemForm {

    private Integer cartNumber;
    private String sku;
    private Integer quantity;
}
