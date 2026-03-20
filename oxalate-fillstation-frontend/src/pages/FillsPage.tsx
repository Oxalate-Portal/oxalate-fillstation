import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Form, Input, InputNumber, DatePicker, Space, Typography, Popconfirm, Tag, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { useTranslation } from 'react-i18next';
import type { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';
import { getFills, createFill, updateFill, deleteFill } from '../api/fillApi';
import type { FillEntry, FillStatus } from '../types';

const { Title } = Typography;

const statusColorMap: Record<FillStatus, string> = {
  ACTIVE: 'green',
  LOCKED: 'orange',
  ZEROED: 'default',
};

const FillsPage: React.FC = () => {
  const { t } = useTranslation();
  const [fills, setFills] = useState<FillEntry[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingFill, setEditingFill] = useState<FillEntry | null>(null);
  const [form] = Form.useForm();
  const [submitLoading, setSubmitLoading] = useState(false);

  const fetchFills = () => {
    setLoading(true);
    getFills()
      .then((res) => setFills(res.data as FillEntry[]))
      .catch(() => message.error(t('common.error')))
      .finally(() => setLoading(false));
  };

  useEffect(fetchFills, []);

  const handleEdit = (record: FillEntry) => {
    setEditingFill(record);
    form.setFieldsValue({ ...record, fillDate: record.fillDate ? dayjs(record.fillDate) : null });
    setModalOpen(true);
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteFill(id);
      message.success(t('common.success'));
      fetchFills();
    } catch {
      message.error(t('common.error'));
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setSubmitLoading(true);
      const payload = { ...values, fillDate: values.fillDate ? (values.fillDate as ReturnType<typeof dayjs>).toISOString() : null };
      if (editingFill) {
        await updateFill(editingFill.id, payload as Partial<FillEntry>);
      } else {
        await createFill(payload as Partial<FillEntry>);
      }
      message.success(t('common.success'));
      setModalOpen(false);
      fetchFills();
    } catch {
      message.error(t('common.error'));
    } finally {
      setSubmitLoading(false);
    }
  };

  const columns: ColumnsType<FillEntry> = [
    { title: t('common.id'), dataIndex: 'id', key: 'id', width: 60 },
    { title: t('fills.date'), dataIndex: 'fillDate', key: 'fillDate', render: (v: string) => v ? new Date(v).toLocaleDateString() : '-' },
    { title: t('fills.startPressure'), dataIndex: 'startPressure', key: 'startPressure' },
    { title: t('fills.endPressure'), dataIndex: 'endPressure', key: 'endPressure' },
    { title: t('fills.o2Added'), dataIndex: 'o2Added', key: 'o2Added' },
    { title: t('fills.heAdded'), dataIndex: 'heAdded', key: 'heAdded' },
    { title: t('fills.gasAdded'), dataIndex: 'gasAdded', key: 'gasAdded' },
    { title: t('fills.status'), dataIndex: 'status', key: 'status', render: (s: FillStatus) => <Tag color={statusColorMap[s]}>{t(`fills.${s.toLowerCase()}`)}</Tag> },
    {
      title: t('common.actions'), key: 'actions',
      render: (_, record) => (
        <Space>
          <Button size="small" onClick={() => handleEdit(record)} disabled={!record.editable}>{t('common.edit')}</Button>
          <Popconfirm title={t('fills.confirmDelete')} onConfirm={() => handleDelete(record.id)} okText={t('common.yes')} cancelText={t('common.no')} disabled={!record.editable}>
            <Button size="small" danger disabled={!record.editable}>{t('common.delete')}</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <Space direction="vertical" style={{ width: '100%' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Title level={2}>{t('fills.title')}</Title>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => { setEditingFill(null); form.resetFields(); setModalOpen(true); }}>{t('fills.add')}</Button>
      </div>
      <Table dataSource={fills} columns={columns} rowKey="id" loading={loading} scroll={{ x: 800 }} />
      <Modal title={editingFill ? t('fills.edit') : t('fills.add')} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)} confirmLoading={submitLoading} okText={t('common.save')} cancelText={t('common.cancel')} width={600}>
        <Form form={form} layout="vertical">
          <Form.Item name="fillDate" label={t('fills.date')}><DatePicker style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="cylinderId" label={t('fills.cylinder')}><InputNumber style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="startPressure" label={t('fills.startPressure')}><InputNumber style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="endPressure" label={t('fills.endPressure')}><InputNumber style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="startO2Percentage" label={t('fills.startO2')}><InputNumber style={{ width: '100%' }} min={0} max={100} /></Form.Item>
          <Form.Item name="startHePercentage" label={t('fills.startHe')}><InputNumber style={{ width: '100%' }} min={0} max={100} /></Form.Item>
          <Form.Item name="endO2Percentage" label={t('fills.endO2')}><InputNumber style={{ width: '100%' }} min={0} max={100} /></Form.Item>
          <Form.Item name="endHePercentage" label={t('fills.endHe')}><InputNumber style={{ width: '100%' }} min={0} max={100} /></Form.Item>
          <Form.Item name="notes" label={t('fills.notes')}><Input.TextArea rows={3} /></Form.Item>
        </Form>
      </Modal>
    </Space>
  );
};

export default FillsPage;
