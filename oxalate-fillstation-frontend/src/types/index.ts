export type UserRole = 'ROLE_ADMIN' | 'ROLE_OPERATOR' | 'ROLE_USER';
export type UserStatus = 'PENDING' | 'ACTIVE' | 'LOCKED';
export type FillStatus = 'ACTIVE' | 'LOCKED' | 'ZEROED';

export interface User {
  id: number;
  name: string;
  email: string;
  language: string;
  status: UserStatus;
  roles: UserRole[];
  createdAt: string;
}

export interface LoginResponse {
  userId: number;
  name: string;
  email: string;
  roles: UserRole[];
  language: string;
}

export interface Cylinder {
  id: number;
  userId: number;
  name: string;
  volume: number;
  workingPressure: number;
  serialNumber: string;
}

export interface FillEntry {
  id: number;
  userId: number;
  cylinderId: number;
  fillDate: string;
  startPressure: number;
  endPressure: number;
  startO2Percentage: number;
  startHePercentage: number;
  endO2Percentage: number;
  endHePercentage: number;
  o2Added: number;
  heAdded: number;
  gasAdded: number;
  notes: string;
  status: FillStatus;
  createdAt: string;
  updatedAt: string;
  editable: boolean;
}

export interface GasUsageSummary {
  totalO2Added: number;
  totalHeAdded: number;
  totalGasAdded: number;
  sinceLastZeroO2Added: number;
  sinceLastZeroHeAdded: number;
  sinceLastZeroGasAdded: number;
}

export interface Configuration {
  id: number;
  groupName: string;
  configKey: string;
  configValue: string;
}

export interface LoginHistoryEntry {
  id: number;
  loginTime: string;
  ipAddress: string;
}
