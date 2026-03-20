import React from 'react';
import { Card, Col, Row, Statistic } from 'antd';
import { useTranslation } from 'react-i18next';
import type { GasUsageSummary } from '../types';

interface GasUsageCardProps {
  data: GasUsageSummary;
}

const GasUsageCard: React.FC<GasUsageCardProps> = ({ data }) => {
  const { t } = useTranslation();

  return (
    <Card title={t('dashboard.gasUsage')}>
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={8}>
          <Statistic title={t('dashboard.totalO2')} value={data.totalO2Added} suffix="L" precision={1} />
        </Col>
        <Col xs={24} sm={8}>
          <Statistic title={t('dashboard.totalHe')} value={data.totalHeAdded} suffix="L" precision={1} />
        </Col>
        <Col xs={24} sm={8}>
          <Statistic title={t('dashboard.totalGas')} value={data.totalGasAdded} suffix="L" precision={1} />
        </Col>
        <Col xs={24} sm={8}>
          <Statistic title={t('dashboard.sinceLastZeroO2')} value={data.sinceLastZeroO2Added} suffix="L" precision={1} />
        </Col>
        <Col xs={24} sm={8}>
          <Statistic title={t('dashboard.sinceLastZeroHe')} value={data.sinceLastZeroHeAdded} suffix="L" precision={1} />
        </Col>
        <Col xs={24} sm={8}>
          <Statistic title={t('dashboard.sinceLastZeroGas')} value={data.sinceLastZeroGasAdded} suffix="L" precision={1} />
        </Col>
      </Row>
    </Card>
  );
};

export default GasUsageCard;
