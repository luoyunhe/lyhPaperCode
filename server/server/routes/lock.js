import 'babel-polyfill';
import Router from 'koa-router';
import { baseApi } from '../config';
import jwt from '../middlewares/jwt';
import LockControllers from '../controllers/lock';


const api = 'lock';

const router = new Router();

router.prefix(`/${baseApi}/${api}`)


router.post('/', LockControllers.bindLock)

router.post('/randomstr', LockControllers.setRandomStr)

router.get('/randomstr', LockControllers.getRandomStr)

router.post('/record', LockControllers.addRecord)

export default router;