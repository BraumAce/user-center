import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';
import { DOCS_LINK } from "@/constants";

const Footer: React.FC = () => {
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      links={[
        {
          key: 'ByteLighting',
          title: 'ByteLighting',
          href: 'https://blog.braumace.cn',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/BraumAce/user-center',
          blankTarget: true,
        },
        {
          key: 'Docs With Building',
          title: 'Docs With Building',
          href: DOCS_LINK,
          blankTarget: true,
        },
      ]}
      copyright={ "2024 - BraumAce" }
    />
  );
};

export default Footer;
