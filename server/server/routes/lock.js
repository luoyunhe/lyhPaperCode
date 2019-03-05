import 'babel-polyfill';
import Router from 'koa-router';
import { baseApi } from '../config';
import jwt from '../middlewares/jwt';
import LockControllers from '../controllers/lock';


const api = 'lock';

const router = new Router();

router.prefix(`/${baseApi}/${api}`)


router.post('/salt', jwt, LockControllers.getSalt);

router.post('/randomstr', LockControllers.setRandomStr);

router.get('/randomstr', LockControllers.getRandomStr);

router.post('/record', LockControllers.addRecord);

router.post('/import', LockControllers.genImportLock);

router.put('/import', LockControllers.setImportLock);

router.get('/import', LockControllers.getImportLock);
export default router;