const { GraphQLObjectType, GraphQLString, GraphQLFloat, GraphQLSchema, GraphQLList, GraphQLNonNull } = require('graphql');
const fs = require('fs');
const path = require('path');

const dataPath = path.join(__dirname, 'data', 'accounts.json');
function loadData() {
  try {
    return JSON.parse(fs.readFileSync(dataPath, 'utf8'));
  } catch (e) {
    return { accounts: [], movements: [] };
  }
}

const AccountType = new GraphQLObjectType({
  name: 'Account',
  fields: {
    cuenta: { type: GraphQLString },
    ci: { type: GraphQLString },
    nombres: { type: GraphQLString },
    apellidos: { type: GraphQLString },
    saldo: { type: GraphQLFloat },
  },
});

const MovementType = new GraphQLObjectType({
  name: 'Movement',
  fields: {
    id: { type: GraphQLString },
    fecha: { type: GraphQLString },
    cuenta: { type: GraphQLString },
    monto: { type: GraphQLFloat },
    descripcion: { type: GraphQLString },
  },
});

const RootQuery = new GraphQLObjectType({
  name: 'RootQueryType',
  fields: {
    accounts: {
      type: new GraphQLList(AccountType),
      resolve() {
        return loadData().accounts;
      }
    },
    account: {
      type: AccountType,
      args: { cuenta: { type: new GraphQLNonNull(GraphQLString) } },
      resolve(_, args) {
        return loadData().accounts.find(a => a.cuenta === args.cuenta) || null;
      }
    },
    movements: {
      type: new GraphQLList(MovementType),
      resolve() {
        return loadData().movements;
      }
    }
  }
});

const Mutation = new GraphQLObjectType({
  name: 'Mutation',
  fields: {
    updateBalance: {
      type: AccountType,
      args: {
        cuenta: { type: new GraphQLNonNull(GraphQLString) },
        saldo: { type: new GraphQLNonNull(GraphQLFloat) },
      },
      resolve(_, { cuenta, saldo }) {
        const data = loadData();
        const idx = data.accounts.findIndex(a => a.cuenta === cuenta);
        if (idx === -1) throw new Error('Account not found');
        data.accounts[idx].saldo = saldo;
        fs.writeFileSync(dataPath, JSON.stringify(data, null, 2), 'utf8');
        return data.accounts[idx];
      }
    },
    addMovement: {
      type: MovementType,
      args: {
        fecha: { type: new GraphQLNonNull(GraphQLString) },
        cuenta: { type: new GraphQLNonNull(GraphQLString) },
        monto: { type: new GraphQLNonNull(GraphQLFloat) },
        descripcion: { type: GraphQLString },
      },
      resolve(_, { fecha, cuenta, monto, descripcion }) {
        const data = loadData();
        const id = 'mov_' + Date.now().toString(36) + '_' + Math.random().toString(36).slice(2,8);
        const movement = { id, fecha, cuenta, monto, descripcion };
        data.movements.push(movement);
        fs.writeFileSync(dataPath, JSON.stringify(data, null, 2), 'utf8');
        return movement;
      }
    },
    createAccount: {
      type: AccountType,
      args: {
        cuenta: { type: new GraphQLNonNull(GraphQLString) },
        ci: { type: new GraphQLNonNull(GraphQLString) },
        nombres: { type: new GraphQLNonNull(GraphQLString) },
        apellidos: { type: new GraphQLNonNull(GraphQLString) },
        saldo: { type: new GraphQLNonNull(GraphQLFloat) },
      },
      resolve(_, args) {
        const data = loadData();
        if (data.accounts.find(a => a.cuenta === args.cuenta)) {
          throw new Error('Account already exists');
        }
        const acc = { ...args };
        data.accounts.push(acc);
        fs.writeFileSync(dataPath, JSON.stringify(data, null, 2), 'utf8');
        return acc;
      }
    }
  }
});

module.exports = new GraphQLSchema({ query: RootQuery, mutation: Mutation });
