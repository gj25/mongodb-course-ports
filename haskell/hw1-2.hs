{-# LANGUAGE OverloadedStrings, ExtendedDefaultRules #-}
module Main (main) where

import Database.MongoDB
import qualified Database.MongoDB as M
import Control.Monad.Trans (liftIO)
import Control.Monad (liftM)

main = do
    pipe <- runIOE $ connect (host "127.0.0.1")
    e1 <- access pipe master "m101" runDocs
    close pipe
    case e1 of
      Left s  -> fail $ show s
      Right i -> print $ "The answer to Homework One, Problem 2 is " ++ show i

runDocs :: Action IO Int
runDocs = do
  docs <- find (select [] "funnynumbers")
  rest docs >>= (\x -> return $ sumValues x)

sumValues :: [Document] -> Int
sumValues d = sum . (filter isMod3) $ d >>= M.lookup "value"
  where isMod3 x = x `mod` 3 == 0