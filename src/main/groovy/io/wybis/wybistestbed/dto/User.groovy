package io.wybis.wybistestbed.dto

import groovy.transform.Canonical
import groovy.transform.ToString
import io.wybis.wybistestbed.model.AbstractModel

@Canonical
@ToString(includeNames = true)
class User {

    String userId

    String password
}
