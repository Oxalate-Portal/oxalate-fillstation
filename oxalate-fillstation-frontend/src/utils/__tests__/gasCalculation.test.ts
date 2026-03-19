import { calculateGasAdded, calculateComponentGasAdded, validateGasPercentages } from '../gasCalculation';

describe('calculateGasAdded', () => {
  it('calculates gas added correctly', () => {
    expect(calculateGasAdded(12, 50, 200)).toBe(1800);
  });
  it('returns 0 when end pressure is less than start pressure', () => {
    expect(calculateGasAdded(12, 200, 100)).toBe(0);
  });
  it('returns 0 when pressures are equal', () => {
    expect(calculateGasAdded(12, 200, 200)).toBe(0);
  });
});

describe('calculateComponentGasAdded', () => {
  it('calculates O2 component gas correctly', () => {
    const result = calculateComponentGasAdded(1800, 32, 21);
    expect(result).toBeCloseTo(477, 1);
  });
  it('returns 0 when gas added is 0', () => {
    expect(calculateComponentGasAdded(0, 32, 21)).toBe(0);
  });
  it('returns 0 when gas added is negative', () => {
    expect(calculateComponentGasAdded(-100, 32, 21)).toBe(0);
  });
});

describe('validateGasPercentages', () => {
  it('returns true for valid percentages', () => {
    expect(validateGasPercentages(32, 25)).toBe(true);
  });
  it('returns false when O2 is negative', () => {
    expect(validateGasPercentages(-1, 25)).toBe(false);
  });
  it('returns false when He is negative', () => {
    expect(validateGasPercentages(32, -1)).toBe(false);
  });
  it('returns false when sum exceeds 100', () => {
    expect(validateGasPercentages(80, 30)).toBe(false);
  });
  it('returns true for pure O2', () => {
    expect(validateGasPercentages(100, 0)).toBe(true);
  });
});
