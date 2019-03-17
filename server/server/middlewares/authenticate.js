import jwt from 'jsonwebtoken';
import { runInContext } from 'vm';
import UserModel from '../models/users'
import { jwtSecret } from '../config'

export default async ctx => {
  const { userName, password } = ctx.request.body;
  if (!userName || !password) {
    ctx.status = 200;
    ctx.body = {
      code: -1,
      msg: 'error'
    }
    return;
  }

  const userFound = await UserModel.find({ name: userName });

  if (userFound.length != 0 && userFound[0].password != password) {
    ctx.status = 401;
    ctx.body = {
      code: -1,
      message: 'Authentication Failed'
    }
    return;
  }


  if (userFound.length == 0) {
    const newUser = new UserModel({ name: userName, password });
    newUser.save();
  }
  ctx.status = 200;
  ctx.body = {
    token: jwt.sign({ userName }, jwtSecret, { expiresIn: 60 * 60 * 24 * 7 }),
    code: 0,
    message: 'Successful Authentication'
  };
};






