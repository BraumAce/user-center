import { Footer } from '@/components';
import { register} from '@/services/ant-design-pro/api';
import { LockOutlined, UserOutlined, } from '@ant-design/icons';
import { LoginForm, ProFormText, } from '@ant-design/pro-components';
import { Helmet, history, useModel } from '@umijs/max';
import { message, Tabs } from 'antd';
import { createStyles } from 'antd-style';
import React, { useState } from 'react';
import Settings from '../../../../config/defaultSettings';
import { SYSTEM_LOGO } from "@/constants";
const useStyles = createStyles(({ token }) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    },
  };
});

const Register: React.FC = () => {
  const [type, setType] = useState<string>('account');
  const { styles } = useStyles();

  const handleSubmit = async (values: API.RegisterParams) => {
    // 校验密码
    const { userName, password, checkPassword } = values;
    if (password !== checkPassword) {
      message.error('两次密码不一致');
      return;
    } else if (userName?.length < 4) {
      message.error('账号长度不能小于4')
      return;
    }

    try {
      // 注册
      const id = await register(values);
      if (id > 0) {
        const RegisterMessage = '注册成功！';
        message.success(RegisterMessage);
        /** 此方法会跳转到 redirect 参数所在的位置 **/
        if (!history) return;
        const { query } = history.location;
        history.push({
          pathname: '/user/login',
          query,
        });
        return;
      } else {
        throw new Error(`register error id = ${id}`);
      }
    } catch (error) {
      const RegisterMessage = '注册失败，请重试！';
      message.error(RegisterMessage);
    }
  };

  return (
    <div className={styles.container}>
      <Helmet>
        <title>
          {'注册'}- {Settings.title}
        </title>
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          logo={<img alt="logo" src={SYSTEM_LOGO} />}
          title="Byte Lighting"
          subTitle={'字节流光 - 一个很酷的学习交流平台'}
          submitter={{
            searchConfig: {
              submitText: '注册',
            }
          }}
          onFinish={async (values) => {
            await handleSubmit(values as API.LoginParams);
          }}
        >
          <Tabs activeKey={type} onChange={setType} centered
            items={[
              {
                key: 'account',
                label: '账号密码注册',
              },
            ]}
          />
          {type === 'account' && (
            <>
              <ProFormText
                name="userName"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined />,
                }}
                placeholder={'请输入账号'}
                rules={[
                  {
                    required: true,
                    message: '账号不能为空！',
                  },
                ]}
              />
              <ProFormText.Password
                name="password"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined />,
                }}
                placeholder={'请输入密码'}
                rules={[
                  {
                    required: true,
                    message: '密码不能为空！',
                  },
                  {
                    min: 8,
                    type: 'string',
                    message: '长度不能小于8',
                  }
                ]}
              />
              <ProFormText.Password
                name="checkPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined />,
                }}
                placeholder={'请确认密码'}
                rules={[
                  {
                    required: true,
                    message: '确认密码不能为空！',
                  },
                  {
                    min: 8,
                    type: 'string',
                    message: '长度不能小于8',
                  }
                ]}
              />
            </>
          )}
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};
export default Register;
