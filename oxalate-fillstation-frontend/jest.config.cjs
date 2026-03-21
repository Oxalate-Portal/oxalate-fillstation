/** @type {import('jest').Config} */
const config = {
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['<rootDir>/src/setupTests.ts'],
  transform: {
    '^.+\\.(ts|tsx)$': ['ts-jest', {
      tsconfig: '<rootDir>/tsconfig.test.json',
    }],
  },
  moduleNameMapper: {
    '\\.(css|less|scss|sass)$': '<rootDir>/__mocks__/styleMock.js',
    '\\.(jpg|jpeg|png|gif|svg)$': '<rootDir>/__mocks__/fileMock.js',
    'buildInfo\\.json$': '<rootDir>/__mocks__/buildInfoMock.js',
  },
  transformIgnorePatterns: [
    '/node_modules/(?!(antd|@ant-design|rc-[a-z-]+|@rc-component)/).*',
  ],
};

module.exports = config;
