package app.service.interfaces;

import app.model.vo.AccountVO;
import dfh.utils.Page;

public interface IAccountService {

    Page queryDayCommissionPageList(AccountVO vo);

}
