import bodyParser from 'koa-bodyparser';
import Koa from 'koa';
import logger from 'koa-logger';
import mongoose from 'mongoose';
import helmet from 'koa-helmet';
import fs from 'fs';
import https from 'https';
import routing from './routes';


import { port, connectionString } from './config';


mongoose.connect(connectionString);
mongoose.connection.on('error', console.error);

// Create Koa Application
const app = new Koa();


app
  .use(logger())
  .use(bodyParser())
  .use(helmet());

routing(app);

// Start the application
const options = {

  key: fs.readFileSync('./ssl/server-key.pem'),

  cert: fs.readFileSync('./ssl/server-cert.pem')

};
https.createServer(options, app.callback()).listen(port, () => {
  console.log(`✅  The server is running at https://localhost:${port}/`)
});

export default app;
