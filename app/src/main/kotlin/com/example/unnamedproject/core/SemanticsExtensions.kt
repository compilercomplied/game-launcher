package com.example.unnamedproject.core

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag

/**
 * A generic utility to encode state and metadata into test tags.
 * Produces tags like "base_id__key1_val1__key2_val2" which are
 * extremely stable for E2E tools like Maestro.
 */
fun Modifier.e2eTag(
    id: String,
    vararg properties: Pair<String, Any?>
): Modifier = semantics(mergeDescendants = true) {
    this.testTag = buildString {
        append(id)
        properties.forEach { (key, value) ->
            if (value != null) {
                // We use double underscore as a separator for readability
                append("__${key}_${value.toString().lowercase()}")
            }
        }
    }
}
