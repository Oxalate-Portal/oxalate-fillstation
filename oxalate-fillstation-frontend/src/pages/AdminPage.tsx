import React, {useCallback, useEffect, useState} from 'react';
import {Button, Form, Input, message, Modal, Popconfirm, Space, Table, Tabs, Tag, Typography} from 'antd';
import {PlusOutlined} from '@ant-design/icons';
import {useTranslation} from 'react-i18next';
import type {ColumnsType} from 'antd/es/table';
import {
    activateAdminUser,
    anonymizeAdminUser,
    closeAdminUser,
    createConfig,
    deleteConfig,
    getAdminUsers,
    getConfig,
    sendAdminPasswordReset,
    updateConfig,
} from '../api/adminApi';
import type {Configuration, User, UserStatus} from '../types';

const { Title } = Typography;

const AdminPage: React.FC = () => {
  const { t } = useTranslation();
  const [configs, setConfigs] = useState<Configuration[]>([]);
    const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
    const [usersLoading, setUsersLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingConfig, setEditingConfig] = useState<Configuration | null>(null);
  const [form] = Form.useForm();
  const [submitLoading, setSubmitLoading] = useState(false);

    const fetchConfigs = useCallback(() => {
        setLoading(true);
        getConfig().then((r) => setConfigs(r.data as Configuration[])).catch(() => message.error(t('common.error'))).finally(() => setLoading(false));
    }, [t]);
    useEffect(() => {
        let active = true;
        void getConfig()
                .then((r) => {
                    if (active) {
                        setConfigs(r.data as Configuration[]);
                    }
                })
                .catch(() => {
                    if (active) {
                        message.error(t('common.error'));
                    }
                })
                .finally(() => {
                    if (active) {
                        setLoading(false);
                    }
                });

        return () => {
            active = false;
        };
    }, [t]);

    const fetchUsers = useCallback(() => {
        setUsersLoading(true);
        getAdminUsers().then((r) => setUsers(r.data as User[])).catch(() => message.error(t('common.error'))).finally(() => setUsersLoading(false));
    }, [t]);

    useEffect(() => {
        let active = true;
        void getAdminUsers()
                .then((r) => {
                    if (active) {
                        setUsers(r.data as User[]);
                    }
                })
                .catch(() => {
                    if (active) {
                        message.error(t('common.error'));
                    }
                })
                .finally(() => {
                    if (active) {
                        setUsersLoading(false);
                    }
                });

        return () => {
            active = false;
        };
    }, [t]);

  const handleEdit = (record: Configuration) => { setEditingConfig(record); form.setFieldsValue(record); setModalOpen(true); };
  const handleDelete = async (id: number) => { try { await deleteConfig(id); message.success(t('common.success')); fetchConfigs(); } catch { message.error(t('common.error')); } };
  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setSubmitLoading(true);
      if (editingConfig) { await updateConfig(editingConfig.id, values as Partial<Omit<Configuration, 'id'>>); } else { await createConfig(values as Omit<Configuration, 'id'>); }
      message.success(t('common.success')); setModalOpen(false); fetchConfigs();
    } catch { message.error(t('common.error')); } finally { setSubmitLoading(false); }
  };

    const handleActivate = async (id: number) => {
        try {
            await activateAdminUser(id);
            message.success(t('common.success'));
            fetchUsers();
        } catch {
            message.error(t('common.error'));
        }
    };

    const handleSendPasswordReset = async (id: number) => {
        try {
            await sendAdminPasswordReset(id);
            message.success(t('admin.passwordResetSent'));
        } catch {
            message.error(t('common.error'));
        }
    };

    const handleCloseAccount = async (id: number) => {
        try {
            await closeAdminUser(id);
            message.success(t('admin.accountClosed'));
            fetchUsers();
        } catch {
            message.error(t('common.error'));
        }
    };

    const handleAnonymize = async (id: number) => {
        try {
            await anonymizeAdminUser(id);
            message.success(t('admin.accountAnonymized'));
            fetchUsers();
        } catch {
            message.error(t('common.error'));
        }
    };

    const renderStatus = (status: UserStatus) => {
        if (status === 'ACTIVE') {
            return <Tag color="green">{status}</Tag>;
        }
        if (status === 'PENDING') {
            return <Tag color="orange">{status}</Tag>;
        }
        return <Tag color="red">{status}</Tag>;
    };

  const columns: ColumnsType<Configuration> = [
    { title: t('common.id'), dataIndex: 'id', key: 'id', width: 60 },
    { title: t('admin.group'), dataIndex: 'groupName', key: 'groupName' },
    { title: t('admin.key'), dataIndex: 'configKey', key: 'configKey' },
    { title: t('admin.value'), dataIndex: 'configValue', key: 'configValue' },
    { title: t('common.actions'), key: 'actions', render: (_, record) => (
      <Space>
        <Button size="small" onClick={() => handleEdit(record)}>{t('common.edit')}</Button>
        <Popconfirm title={t('admin.confirmDelete')} onConfirm={() => handleDelete(record.id)} okText={t('common.yes')} cancelText={t('common.no')}>
          <Button size="small" danger>{t('common.delete')}</Button>
        </Popconfirm>
      </Space>
    )},
  ];

    const userColumns: ColumnsType<User> = [
        {title: t('common.id'), dataIndex: 'id', key: 'id', width: 60},
        {title: t('common.name'), dataIndex: 'name', key: 'name'},
        {title: t('common.email'), dataIndex: 'email', key: 'email'},
        {title: t('common.status'), dataIndex: 'status', key: 'status', render: renderStatus},
        {
            title: t('common.actions'), key: 'actions', render: (_, record) => (
                    <Space>
                        {record.status !== 'ACTIVE' && <Button size="small" onClick={() => handleActivate(record.id)}>{t('admin.activateUser')}</Button>}
                        <Button size="small" onClick={() => handleSendPasswordReset(record.id)}>{t('admin.sendPasswordReset')}</Button>
                        {record.status !== 'CLOSED' && (
                                <Popconfirm
                                        title={t('admin.confirmClose')}
                                        onConfirm={() => handleCloseAccount(record.id)}
                                        okText={t('common.yes')}
                                        cancelText={t('common.no')}
                                >
                                    <Button size="small" danger>{t('admin.closeUser')}</Button>
                                </Popconfirm>
                        )}
                        <Popconfirm
                                title={t('admin.confirmAnonymize')}
                                onConfirm={() => handleAnonymize(record.id)}
                                okText={t('common.yes')}
                                cancelText={t('common.no')}
                        >
                            <Button size="small">{t('admin.anonymizeUser')}</Button>
                        </Popconfirm>
                    </Space>
            ),
        },
    ];

  return (
          <Space orientation="vertical" style={{width: '100%'}}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Title level={2}>{t('admin.title')}</Title>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => { setEditingConfig(null); form.resetFields(); setModalOpen(true); }}>{t('admin.add')}</Button>
      </div>
              <Tabs
                      items={[
                          {key: 'config', label: t('admin.config'), children: <Table dataSource={configs} columns={columns} rowKey="id" loading={loading}/>},
                          {
                              key: 'users',
                              label: t('admin.users'),
                              children: <Table dataSource={users} columns={userColumns} rowKey="id" loading={usersLoading}/>
                          },
                      ]}
              />
      <Modal title={editingConfig ? t('admin.edit') : t('admin.add')} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)} confirmLoading={submitLoading} okText={t('common.save')} cancelText={t('common.cancel')}>
        <Form form={form} layout="vertical">
          <Form.Item name="groupName" label={t('admin.group')} rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="configKey" label={t('admin.key')} rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="configValue" label={t('admin.value')} rules={[{ required: true }]}><Input /></Form.Item>
        </Form>
      </Modal>
    </Space>
  );
};

export default AdminPage;
