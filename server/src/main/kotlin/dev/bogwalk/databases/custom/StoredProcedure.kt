package dev.bogwalk.databases.custom

import dev.bogwalk.models.MAX_PLANET_DISTANCE
import org.jetbrains.exposed.sql.IntegerColumnType
import org.jetbrains.exposed.sql.Transaction

/** Runs a statement that creates a new function for retrieving the max stored planet distance. */
fun Transaction.createDistanceFunction() {
    exec(MAX_DISTANCE_FUNCTION_SQL.trimIndent())
}

/** Retrieves the current max stored planet distance as a [Float] rounded to the specified [precision]. */
fun Transaction.getMaxDistance(
    precision: Int
): Float = exec(
    stmt = SELECT_MAX_DISTANCE_SQL,
    args = listOf(IntegerColumnType() to precision)
) {
    it.next()
    it.getFloat(1) + 1f
} ?: MAX_PLANET_DISTANCE

private const val MAX_DISTANCE_FUNCTION_SQL = """
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
    """

private const val SELECT_MAX_DISTANCE_SQL = "SELECT max_distance(?)"