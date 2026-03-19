import React, { useEffect, useState } from 'react';
import { Button, Modal, Table, Typography, Space, Spin, message } from 'antd';
import { useTranslation } from 'react-i18next';
import type { ColumnsType } from 'antd/es/table';
import GasUsageCard from '../components/GasUsageCard';
import { getGasUsage, getLoginHistory, anonymize } from '../api/userApi';
import type { GasUsageSummary, LoginHistoryEntry } from '../types';

const { Title } = Typography;

const DashboardPage: React.FC = () => {
  const { t } = useTranslation();
  const [gasUsage, setGasUsage] = useState<GasUsageSummary | null>(null);
  const [loginHistory, setLoginHistory] = useState<LoginHistoryEntry[]>([]);
  const [loading, setLoading] = useState(true);
  const [anonymizeModalOpen, setAnonymizeModalOpen] = useState(false);
  const [anonymizeLoading, setAnonymizeLoading] = useState(false);

  useEffect(() => {
    Promise.all([getGasUsage(), getLoginHistory()])
      .then(([gasRes, historyRes]) => {
        setGasUsage(gasRes.data as GasUsageSummary);
        setLoginHistory(historyRes.data as LoginHistoryEntry[]);
      })
      .catch(() => message.error(t('common.error')))
      .finally(() => setLoading(false));
  }, [t]);

  const handleAnonymize = async () => {
    setAnonymizeLoading(true);
    try {
      await anonymize();
      message.success(t('dashboard.anonymizeSuccess'));
      window.location.href = '/login';
    } catch {
      message.error(t('common.error'));
    } finally {
      setAnonymizeLoading(false);
      setAnonymizeModalOpen(false);
    }
  };

  const columns: ColumnsType<LoginHistoryEntry> = [
    { title: t('common.id'), dataIndex: 'id', key: 'id', width: 80 },
    { title: t('dashboard.loginTime'), dataIndex: 'loginTime', key: 'loginTime' },
    { title: t('dashboard.ipAddress'), dataIndex: 'ipAddress', key: 'ipAddress' },
  ];

  if (loading) return <Spin size="large" />;

  return (
    <Space direction="vertical" style={{ width: '100%' }} size="large">
      <Title level={2}>{t('dashboard.title')}</Title>
      {gasUsage && <GasUsageCard data={gasUsage} />}
      <Title level={4}>{t('dashboard.loginHistory')}</Title>
      <Table dataSource={loginHistory} columns={columns} rowKey="id" size="small" />
      <Button danger onClick={() => setAnonymizeModalOpen(true)}>{t('dashboard.anonymize')}</Button>
      <Modal
        title={t('dashboard.anonymize')}
        open={anonymizeModalOpen}
        onOk={handleAnonymize}
        onCancel={() => setAnonymizeModalOpen(false)}
        confirmLoading={anonymizeLoading}
        okType="danger"
        okText={t('common.confirm')}
      >
        {t('dashboard.anonymizeConfirm')}
      </Modal>
    </Space>
  );
};

export default DashboardPage;
