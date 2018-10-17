import React from 'react';

import { Row, Col } from 'react-bootstrap';

import { PageHeader } from 'components/common';

const Teste = React.createClass({
  render() {
    return (
      <PageHeader title="Teste">
        <span>
          teste
        </span>
      </PageHeader>
    );
  }
});

export default Teste;