import React, { useEffect, useState } from 'react';
import { Tabs, Table, Button, Space, Typography, Tag, Popconfirm, message } from 'antd';
import { useTranslation } from 'react-i18next';
import type { ColumnsType } from 'antd/es/table';
import { getUsers, getPendingRegistrations, approveRegistration, rejectRegistration, updateUserStatus, zeroUserFills, notifyUsers } from '../api/operatorApi';
import type { User, UserStatus } from '../types';

const { Title } = Typography;

const OperatorPage: React.FC = () => {
  const { t } = useTranslation();
  const [users, setUsers] = useState<User[]>([]);
  const [pendingUsers, setPendingUsers] = useState<User[]>([]);
  const [usersLoading, setUsersLoading] = useState(true);
  const [pendingLoading, setPendingLoading] = useState(true);
  const [notifyLoading, setNotifyLoading] = useState(false);

  const fetchUsers = () => { setUsersLoading(true); getUsers().then((r) => setUsers(r.data as User[])).catch(() => message.error(t('common.error'))).finally(() => setUsersLoading(false)); };
  const fetchPending = () => { setPendingLoading(true); getPendingRegistrations().then((r) => setPendingUsers(r.data as User[])).catch(() => message.error(t('common.error'))).finally(() => setPendingLoading(false)); };

  useEffect(() => { fetchUsers(); fetchPending(); }, []);

  const handleApprove = async (id: number) => { try { await approveRegistration(id); message.success(t('operator.approveSuccess')); fetchPending(); } catch { message.error(t('common.error')); } };
  const handleReject = async (id: number) => { try { await rejectRegistration(id); message.success(t('operator.rejectSuccess')); fetchPending(); } catch { message.error(t('common.error')); } };
  const handleUpdateStatus = async (id: number, status: UserStatus) => { try { await updateUserStatus(id, status); message.success(t('common.success')); fetchUsers(); } catch { message.error(t('common.error')); } };
  const handleZeroFills = async (userId: number) => { try { await zeroUserFills(userId); message.success(t('operator.zeroFillsSuccess')); } catch { message.error(t('common.error')); } };
  const handleNotify = async () => { setNotifyLoading(true); try { await notifyUsers(); message.success(t('operator.notifySuccess')); } catch { message.error(t('common.error')); } finally { setNotifyLoading(false); } };

  const userColumns: ColumnsType<User> = [
    { title: t('common.id'), dataIndex: 'id', key: 'id', width: 60 },
    { title: t('common.name'), dataIndex: 'name', key: 'name' },
    { title: t('common.email'), dataIndex: 'email', key: 'email' },
    { title: t('common.status'), dataIndex: 'status', key: 'status', render: (s: UserStatus) => <Tag color={s === 'ACTIVE' ? 'green' : s === 'LOCKED' ? 'red' : 'orange'}>{s}</Tag> },
    { title: t('common.actions'), key: 'actions', render: (_, record) => (
      <Space>
        {record.status !== 'ACTIVE' && <Button size="small" onClick={() => handleUpdateStatus(record.id, 'ACTIVE')}>{t('operator.activate')}</Button>}
        {record.status !== 'LOCKED' && <Button size="small" danger onClick={() => handleUpdateStatus(record.id, 'LOCKED')}>{t('operator.lock')}</Button>}
        <Popconfirm title={t('operator.zeroFills')} onConfirm={() => handleZeroFills(record.id)} okText={t('common.yes')} cancelText={t('common.no')}>
          <Button size="small">{t('operator.zeroFills')}</Button>
        </Popconfirm>
      </Space>
    )},
  ];

  const pendingColumns: ColumnsType<User> = [
    { title: t('common.id'), dataIndex: 'id', key: 'id', width: 60 },
    { title: t('common.name'), dataIndex: 'name', key: 'name' },
    { title: t('common.email'), dataIndex: 'email', key: 'email' },
    { title: t('common.date'), dataIndex: 'createdAt', key: 'createdAt' },
    { title: t('common.actions'), key: 'actions', render: (_, record) => (
      <Space>
        <Button type="primary" size="small" onClick={() => handleApprove(record.id)}>{t('operator.approve')}</Button>
        <Button danger size="small" onClick={() => handleReject(record.id)}>{t('operator.reject')}</Button>
      </Space>
    )},
  ];

  return (
    <Space direction="vertical" style={{ width: '100%' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Title level={2}>{t('operator.title')}</Title>
        <Button onClick={handleNotify} loading={notifyLoading}>{t('operator.notifyUsers')}</Button>
      </div>
      <Tabs items={[
        { key: 'users', label: t('operator.users'), children: <Table dataSource={users} columns={userColumns} rowKey="id" loading={usersLoading} /> },
        { key: 'pending', label: t('operator.pendingRegistrations'), children: <Table dataSource={pendingUsers} columns={pendingColumns} rowKey="id" loading={pendingLoading} /> },
      ]} />
    </Space>
  );
};

export default OperatorPage;
