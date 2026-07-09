/** @type {import('jest').Config} */
const config = {
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['<rootDir>/src/setupTests.ts'],
  transform: {
    '^.+\\.(ts|tsx)$': ['ts-jest', {
      tsconfig: '<rootDir>/tsconfig.test.json',
    }],
      '^.+\\.jsx?$': ['babel-jest', {}],
  },
  moduleNameMapper: {
    '\\.(css|less|scss|sass)$': '<rootDir>/__mocks__/styleMock.js',
    '\\.(jpg|jpeg|png|gif|svg)$': '<rootDir>/__mocks__/fileMock.js',
    'buildInfo\\.json$': '<rootDir>/__mocks__/buildInfoMock.js',
  },
  transformIgnorePatterns: [
      'node_modules/(?!(@ant-design|antd|rc-.*|@rc-component|@babel/runtime)/)',
  ],
};

module.exports = config;
