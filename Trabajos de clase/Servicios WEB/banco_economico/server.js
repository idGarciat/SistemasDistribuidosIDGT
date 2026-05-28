const express = require('express');
const { graphqlHTTP } = require('express-graphql');
const schema = require('./schema');
const fs = require('fs');
const path = require('path');

const DATA_PATH = path.join(__dirname, 'data', 'accounts.json');
const PORT = process.env.PORT || 3002;

const app = express();
app.use(express.json());

app.get('/', (req, res) => {
  res.json({ service: 'Banco Economico', version: '1.0.0', graphql: '/graphql' });
});

app.use('/graphql', graphqlHTTP({
  schema,
  graphiql: true,
}));

app.listen(PORT, () => {
  console.log(`Banco Económico GraphQL running on http://127.0.0.1:${PORT}/graphql`);
});
