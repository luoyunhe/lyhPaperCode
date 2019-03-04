import jwt from 'koa-jwt';
import { jwtSecret } from '../config'

export default jwt({
  secret: jwtSecret
});
