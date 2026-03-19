/**
 * Calculate the volume of gas added to a cylinder during a fill.
 */
export function calculateGasAdded(cylinderVolume: number, startPressure: number, endPressure: number): number {
  if (endPressure <= startPressure) return 0;
  return cylinderVolume * (endPressure - startPressure);
}

/**
 * Calculate the volume of a specific gas component added.
 */
export function calculateComponentGasAdded(gasAdded: number, endPercentage: number, startPercentage: number): number {
  if (gasAdded <= 0) return 0;
  const avgPercentage = (endPercentage + startPercentage) / 2;
  return (gasAdded * avgPercentage) / 100;
}

/**
 * Validate that gas percentages are within acceptable ranges.
 */
export function validateGasPercentages(o2Percentage: number, hePercentage: number): boolean {
  if (o2Percentage < 0 || o2Percentage > 100) return false;
  if (hePercentage < 0 || hePercentage > 100) return false;
  if (o2Percentage + hePercentage > 100) return false;
  return true;
}
