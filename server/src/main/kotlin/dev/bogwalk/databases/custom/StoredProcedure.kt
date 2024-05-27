package dev.bogwalk.databases.custom

import org.jetbrains.exposed.sql.IntegerColumnType
import org.jetbrains.exposed.sql.Transaction

/** Runs a statement that creates a new function for retrieving the max stored planet distance. */
fun Transaction.createDistanceFunction() {
    exec(
        """
        CREATE OR REPLACE FUNCTION max_distance(
            IN prec INT
        ) RETURNS NUMERIC
        LANGUAGE plpgsql IMMUTABLE AS $$
            DECLARE max_distance NUMERIC;
            BEGIN
                SELECT
                ROUND(CAST(MAX(distance) AS numeric), prec)
                INTO max_distance
                FROM planets;
                IF max_distance > 100000 THEN
                    max_distance := 100000;
                END IF;
                RETURN max_distance;
            END;
        $$;
        """.trimIndent()
    )
}

/** Retrieves the current max stored planet distance as a [Float] rounded to the specified [precision]. */
fun Transaction.getMaxDistance(
    precision: Int
): Float = exec(
    stmt = "SELECT max_distance(?)",
    args = listOf(IntegerColumnType() to precision)
) {
    it.next()
    it.getFloat(1)
} ?: 60000f