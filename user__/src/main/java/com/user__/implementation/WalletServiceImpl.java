package com.user__.implementation;



import com.user__.entity.User;
import com.user__.entity.Wallet;
import com.user__.exception.AddServiceErrorMessage;
import com.user__.exception.AmountErrorMessage;
import com.user__.exception.WalletErrorMessage;
import com.user__.repository.UserRepository;
import com.user__.repository.WalletRepository;
import com.user__.request.WalletCreationRequest;
import com.user__.request.WalletFundingRequest;
import com.user__.response.FundingResponse;
import com.user__.service.WalletService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseEntity<?> createWallet(WalletCreationRequest walletCreationRequest) {
        User user = userRepository.findByEmail(walletCreationRequest.getEmail()).get();
        Wallet wallet = new Wallet();
        wallet.setWalletName(walletCreationRequest.getWalletName());
        wallet.setBalance(0.00);

        user.setWallet(wallet);
        wallet.setUser(user);


        walletRepository.save(wallet);
        return ResponseEntity.ok("Wallet creation successful");

    }

    @Override
    public ResponseEntity<?> fundWallet(WalletFundingRequest fundingRequest) {
        if(fundingRequest.getWalletName().equals("")){
            WalletErrorMessage walletErrorMessage= new WalletErrorMessage();
            walletErrorMessage.setWalletErrorMessage("Wallet name cannot be empty");
            return ResponseEntity.badRequest().body(walletErrorMessage);
        }
        else if(fundingRequest.getAmount()==null){
            AmountErrorMessage amountErrorMessage= new AmountErrorMessage();
           amountErrorMessage.setAmountErrorMessage("Wallet amount cannot be empty");
            return ResponseEntity.badRequest().body(amountErrorMessage);
        }
       else if (fundingRequest.getAmount() >= 1) {
            FundingResponse fundingResponse= new FundingResponse();
            Wallet wallet = walletRepository.findByWalletName(fundingRequest.getWalletName());
            String funderEmail = wallet.getUser().getEmail();
            wallet.setBalance(fundingRequest.getAmount()+wallet.getBalance());
            walletRepository.save(wallet);
            String walletName= wallet.getWalletName();
            Double balance= wallet.getBalance();
            fundingResponse.setBalance(balance);
            fundingResponse.setWalletName(walletName);
            fundingResponse.setFunderEmail(funderEmail);
            fundingResponse.setFundingAmount(fundingRequest.getAmount());

            System.out.println("Funding amount: "+fundingResponse.getFundingAmount());

            rabbitTemplate.convertAndSend("Light-Weight-App-Funding",fundingResponse);
            return ResponseEntity.ok("Funding successful");

        }

        else {
            AmountErrorMessage amountErrorMessage= new AmountErrorMessage();
            amountErrorMessage.setAmountErrorMessage("amount");
            amountErrorMessage.setAmountErrorMessage("Amount must be at least 1 naira");
            return ResponseEntity.badRequest().body(amountErrorMessage);
        }
    }
}
